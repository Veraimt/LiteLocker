package de.veraimt.litelocker.signs;

import com.mojang.authlib.GameProfile;
import de.veraimt.litelocker.LiteLocker;
import de.veraimt.litelocker.protection.protectable.ProtectableBlockContainer;
import de.veraimt.litelocker.protection.protector.Protector;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.SignText;

import java.util.Optional;
import java.util.UUID;

import static de.veraimt.litelocker.LiteLocker.LOGGER;

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


    @Override
    default boolean isValid() {
        var firstLine = getBlockEntity().getFrontText().getMessage(0, false);

        Tag tag = Tag.fromString(firstLine.getString());

        if (tag == null) {
            return false;
        }

        return Protector.super.isValid();
    }

    @Override
    default void activate() {
        System.out.println("activate");
        Component firstLine = getBlockEntity().getFrontText().getMessage(0, false);

        Tag tag = Tag.fromString(firstLine.getString());

        if (tag == null) {
            Protector.super.deactivate();
            return;
        }

        ProtectableBlockContainer container = getAttachedContainer();

        if (container == null) {
            return;
        }

        if (container.hasProtector()) {
            if (!container.hasProtector(this)) {
                firstLine = Component.nullToEmpty(Tag.MORE_USERS.tag);
                getBlockEntity().setText(getBlockEntity().getFrontText().setMessage(0, firstLine.copy().withStyle(ChatFormatting.BOLD)), true);
            }
        } else {
            setMain();
        }
        updateGameProfiles(null);

        Protector.super.activate();
    }

    @Override
    default void deactivate() {
        Protector.super.deactivate();
        var signText = getBlockEntity().getFrontText();
        var messages = signText.getMessages(false);
        for (int i = 0; i < messages.length; i++) {
            messages[i] = Component.nullToEmpty(messages[i].getString());
        }
        getBlockEntity().setText(new SignText(messages, messages, signText.getColor(), signText.hasGlowingText()), true);
    }

    default void updateGameProfiles(final Runnable onComplete) {
        //System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
        new Thread(() -> {
            var serverProfileCache = LiteLocker.server.getProfileCache();

            if (serverProfileCache == null) {
                LOGGER.error("ServerProfileCache is null!");
                return;
            }

            var users = getUsers();
            var signText = getBlockEntity().getFrontText();

            var messageComponents = signText.getMessages(false);
            for (int i = 0; i < users.length; i++) {
                var messageIndex = i+1;
                Component message = signText.getMessage(messageIndex, false);
                var messageString = message.getString();

                //OwnerLine: Main sign and first line
                var preventUserModification = messageIndex == 1 && isMain() && !LiteLocker.config.getCanRemoveSignCreator();

                Optional<GameProfile> gameProfile;
                if (messageString.isBlank()) {
                    //Message blank
                    if (preventUserModification) {
                        //empty Optional to get GameProfile by UUID
                        gameProfile = Optional.empty();
                    } else {
                        removeUser(i);
                        continue;
                    }
                } else {
                    //Message not blank
                    //if handling OwnerLine and a UUID is present using empty Optional to get GameProfile by UUID
                    //otherwise get GameProfile by Name

                    if (preventUserModification && users[i] != null) {
                        gameProfile = Optional.empty();
                    } else {
                        gameProfile = serverProfileCache.get(messageString);
                    }
                }

                //GameProfile by Name
                if (gameProfile.isPresent()) {
                    final UUID playerUUID = gameProfile.get().getId();

                    users[i] = playerUUID;
                } else {
                    //GameProfile by UUID

                    gameProfile = serverProfileCache.get(users[i]);

                    if (gameProfile.isEmpty()) {
                        messageComponents[messageIndex] = Component.nullToEmpty(messageString)
                                .copy().withStyle(ChatFormatting.STRIKETHROUGH);
                        removeUser(i);
                        continue;
                    }
                }

                ChatFormatting[] style;
                if (preventUserModification) {
                    style = new ChatFormatting[] {ChatFormatting.ITALIC, ChatFormatting.UNDERLINE};
                } else {
                    style = new ChatFormatting[] {ChatFormatting.ITALIC};
                }
                messageComponents[messageIndex] = Component.nullToEmpty(gameProfile.get().getName())
                        .copy().withStyle(style);
            }


            getBlockEntity().setText(new SignText(messageComponents, messageComponents, signText.getColor(), signText.hasGlowingText()), true);

            if (onComplete != null)
                onComplete.run();
        }).start();
    }

    @Override
    default void removeUser(int i) {
        Protector.super.removeUser(i);
        getBlockEntity().getFrontText().setMessage(i+1, Component.nullToEmpty(getBlockEntity().getFrontText().getMessage(i+1, false).getString()));
    }
}
