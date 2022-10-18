package de.veraimt.litelocker.protection.protector;

import com.mojang.authlib.GameProfile;
import de.veraimt.litelocker.protection.protectable.ProtectableBlockContainer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.SignBlockEntity;

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
        return Tag.PRIVATE.tag.equals(getBlockEntity().getMessage(0, false).getString());
    }

    @Override
    default boolean isValid() {
        var firstLine = getBlockEntity().getMessage(0, false);

        Tag tag = Tag.fromString(firstLine.getString());

        if (tag == null) {
            return false;
        }

        return Protector.super.isValid();
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    default void activate() {
        Component firstLine = getBlockEntity().getMessage(0, false);

        Tag tag = Tag.fromString(firstLine.getString());

        if (tag == null) {
            Protector.super.deactivate();
            return;
        }

        final Level world = getBlockEntity().getLevel();
        ProtectableBlockContainer container = getAttachedContainer();

        if (container == null) {
            return;
        }

        if (container.hasProtector() && !container.hasProtector(this)) {
            if (!tag.equals(Tag.MORE_USERS)) {
                firstLine = Component.nullToEmpty(Tag.MORE_USERS.tag);
            }
        }
        new Thread(() -> {
            for (int i = 1; i < SignBlockEntity.LINES; i++) {
                Component message = getBlockEntity().getMessage(i, false);

                //Getting Player UUID from Server that the world runs on
                final UUID playerUUID = world.getServer().getProfileCache().get(message.getString())
                        .map(GameProfile::getId).orElse(null);

                if (playerUUID != null) {
                    getUsers()[i-1] = playerUUID;

                    getBlockEntity().setMessage(i, message.copy().withStyle(ChatFormatting.ITALIC));
                } else {
                    removeUser(i-1);
                }
            }
            updateWorld();
        }).start();

        Protector.super.activate();
        //Turning first line bold as indicator that the Protection is active
        getBlockEntity().setMessage(0, firstLine.copy().withStyle(ChatFormatting.BOLD));
        //getBlockEntity().getLevel().sendBlockUpdated(getBlockEntity().getBlockPos(), getBlockEntity().getBlockState(), getBlockEntity().getBlockState(), 3);
    }

    @SuppressWarnings("ConstantConditions")
    default void updateWorld() {
        var blockEntity = getBlockEntity();
        blockEntity.getLevel().sendBlockUpdated(blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity.getBlockState(), 3);
    }
}
