package de.veraimt.litelocker.signs;

import com.mojang.authlib.GameProfile;
import de.veraimt.litelocker.LiteLocker;
import de.veraimt.litelocker.protection.Protector;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.SignText;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Consumer;

public interface ProtectorSign extends Protector<SignBlockEntity> {

    enum Tag {
        PRIVATE("[Private]"),
        MORE_USERS("[More Users]");

        public final String tag;
        Tag(String tag) {
            this.tag = tag;
        }

        public static Tag fromString(String s) {
            for (var t : values()) {
                if (t.tag.equalsIgnoreCase(s))
                    return t;
            }
            return null;
        }
    }

    @Override
    SignBlockEntity getBlockEntity();

    default boolean hasTag() {
        var firstLine = getMessage(0);

        Tag tag = Tag.fromString(firstLine.getString());

        return tag != null;
    }

    @Override
    default boolean isValid() {
        return hasTag() && Protector.super.isValid();
    }

    default void activate() {
        //TODO remove debug
        System.out.println("activate");
        if (!isValid()) {
            deactivate();
            return;
        }

        updateGameProfilesOnUpdate(signText -> {
            getBlockEntity().setText(signText, true);


            Component firstLine = getMessage(0).copy().withStyle(ChatFormatting.BOLD);
            getBlockEntity().setText(getBlockEntity().getFrontText().setMessage(0, firstLine), true);

            //TODO remove debug
            System.out.println("activate complete");
        });


    }

    default void deactivate() {
        var signText = getBlockEntity().getFrontText();
        var messages = signText.getMessages(false);
        for (int i = 0; i < messages.length; i++) {
            messages[i] = messages[i].plainCopy();
        }
        getBlockEntity().setText(new SignText(messages, messages, signText.getColor(), signText.hasGlowingText()), true);
    }

    default void updateGameProfilesOnLoad(Consumer<SignText> signTextConsumer) {
        //TODO remove debug
        System.out.println("updateGameProfilesOnLoad");
        System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
        updateGameProfiles(signTextConsumer, false);
    }

    default void updateGameProfilesOnUpdate(Consumer<SignText> signTextConsumer) {
        //TODO remove debug
        System.out.println("updateGameProfilesOnUpdate");
        updateGameProfiles(signTextConsumer, true);
    }


    default void updateGameProfiles(Consumer<SignText> signTextConsumer, boolean byName) {
        //TODO use an executor instead of creating a new thread
        new Thread(() -> {
            var users = getUsers();
            var signText = getBlockEntity().getFrontText();
            var serverProfileCache = LiteLocker.getServer().getProfileCache();

            var messageComponents = signText.getMessages(false);
            for (int i = 0; i < users.length; i++) {
                var messageIndex = i+1;
                var messageString = signText.getMessage(messageIndex, false).getString();

                //TODO remove debug
                System.out.println("messageString: " + messageString);

                if (messageString.isBlank()) {//no text -> remove
                    removeUser(i);
                    continue;
                }

                Optional<GameProfile> gameProfile = Optional.empty();
                if (users[i] != null && !byName) { //UUID present do lookup via it
                    gameProfile = serverProfileCache.get(users[i]);
                }

                //TODO remove debug
                System.out.println("gameProfile by UUID: " + gameProfile);

                if (gameProfile.isEmpty()) {
                    gameProfile = serverProfileCache.get(messageString);
                }

                //TODO remove debug
                System.out.println("gameProfile: " + gameProfile);

                if (gameProfile.isEmpty()) {
                    //make text strikethrough so that the users sees that this name is invalid
                    messageComponents[messageIndex] = messageComponents[messageIndex].plainCopy().withStyle(ChatFormatting.STRIKETHROUGH);
                    removeUser(i);
                    continue;
                }

                //make text italic as indication of success
                messageComponents[messageIndex] = Component.literal(gameProfile.get().getName()).withStyle(ChatFormatting.ITALIC);
                users[i] = gameProfile.get().getId();
            }
            signText = new SignText(messageComponents, messageComponents, signText.getColor(), signText.hasGlowingText());
            signTextConsumer.accept(signText);
        }).start();
    }

    default Component getMessage(int index) {
        return getBlockEntity().getFrontText().getMessage(index, false);
    }

    @Override
    default boolean shouldSave() {
        boolean anyUsers = false;
        for (var user : getUsers()) {
            if (user != null) {
                anyUsers = true;
                break;
            }
        }
        return anyUsers;
    }
}
