package de.veraimt.litelocker.signs;

import de.veraimt.litelocker.LiteLocker;
import de.veraimt.litelocker.protection.protectable.ProtectableBlockContainer;
import de.veraimt.litelocker.protection.protector.Protector;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.SignText;

import java.util.Arrays;
import java.util.UUID;

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
    default boolean isMain() {
        return Tag.PRIVATE.tag.equalsIgnoreCase(getBlockEntity().getFrontText().getMessage(0, false).getString());
    }

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
        System.out.println(firstLine);
        System.out.println(firstLine.getString());

        Tag tag = Tag.fromString(firstLine.getString());

        if (tag == null) {
            System.out.println("tag null");
            Protector.super.deactivate();
            return;
        }

        ProtectableBlockContainer container = getAttachedContainer();

        if (container == null) {
            return;
        }

        if (container.hasProtector()) {
            if (!container.hasProtector(this)) {
                tag = Tag.MORE_USERS;
            }
        } else {
            tag = Tag.PRIVATE;
        }
        updateGameProfiles(null);

        Protector.super.activate();
        //Turning first line bold as indicator that the Protection is active
        firstLine = Component.nullToEmpty(tag.tag);
        setTextOverride(getBlockEntity().getFrontText().setMessage(0, firstLine.copy().withStyle(ChatFormatting.BOLD)), true);
    }

    @Override
    default void deactivate() {
        Protector.super.deactivate();
        var signText = getBlockEntity().getFrontText();
        var messages = signText.getMessages(false);
        for (int i = 0; i < messages.length; i++) {
            messages[i] = Component.nullToEmpty(messages[i].getString());
        }
        setTextOverride(new SignText(messages, messages, signText.getColor(), signText.hasGlowingText()), true);
    }

    default void updateGameProfiles(final Runnable onComplete) {
        System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
        new Thread(() -> {
            var users = getUsers();
            var signText = getBlockEntity().getFrontText();

            var messageComponents = signText.getMessages(false);
            for (int i = 1; i < users.length; i++) {
                Component message = signText.getMessage(i, false);

                var gameProfile = LiteLocker.server.getProfileCache().get(message.getString());

                if (gameProfile.isPresent()) {
                    System.out.println(gameProfile);
                    final UUID playerUUID = gameProfile.get().getId();

                    users[i-1] = playerUUID;

                    messageComponents[i] = Component.nullToEmpty(gameProfile.get().getName())
                            .copy().withStyle(ChatFormatting.ITALIC);

                } else {
                    removeUser(i-1);
                }
            }


            setTextOverride(new SignText(messageComponents, messageComponents, signText.getColor(), signText.hasGlowingText()), true);

            if (onComplete != null)
                onComplete.run();
        }).start();
    }

    @Override
    default void removeUser(int i) {
        Protector.super.removeUser(i);
        getBlockEntity().getFrontText().setMessage(i+1, Component.nullToEmpty(getBlockEntity().getFrontText().getMessage(i+1, false).getString()));
    }


    void setTextOverride(SignText signText, boolean frontSide);
}
