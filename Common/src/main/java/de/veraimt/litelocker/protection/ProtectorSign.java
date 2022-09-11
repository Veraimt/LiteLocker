package de.veraimt.litelocker.protection;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.SignBlockEntity;

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
        return getBlockEntity().getMessage(0, false).getContents().equals(Tag.PRIVATE.tag);
    }

    @Override
    default boolean isValid() {
        Component firstLine = getBlockEntity().getMessage(0, false);

        Tag tag = Tag.fromString(firstLine.getContents());

        if (tag == null) {
            System.out.println("wrong tag");
            return false;
        }

        return Protector.super.isValid();
    }

    @Override
    default void activate() {
        System.out.println("Activate");
        Component firstLine = getBlockEntity().getMessage(0, false);

        Tag tag = Tag.fromString(firstLine.getContents());

        if (tag == null) {
            System.out.println("wrong tag");
            deactivate();
            return;
        }

        Level world = getBlockEntity().getLevel();
        ProtectableContainer container = getAttachedContainer();

        if (container == null) {
            System.out.println("not a container!");
            return;
        }

        if (container.hasProtector() && !container.hasProtector(this)) {
            if (!tag.equals(Tag.MORE_USERS)) {
                firstLine = Component.nullToEmpty(Tag.MORE_USERS.tag);
            }
        }

        for (int i = 1; i < SignBlockEntity.LINES; i++) {
            Component message = getBlockEntity().getMessage(i, false);

            //Getting Player from Server that the world runs on
            Player player = world.getServer().getPlayerList().getPlayerByName(
                    message.getContents());
            System.out.println("message: " + message);
            System.out.println("found Player: " + player);
            if (player != null) {
                getUsers()[i-1] = player.getUUID();

                getBlockEntity().setMessage(i, message.copy().withStyle(ChatFormatting.ITALIC));
            } else {
                removeUser(i-1);
            }

        }

        Protector.super.activate();
        //Turning first line bold as indicator that the Protection is active
        getBlockEntity().setMessage(0, firstLine.copy().withStyle(ChatFormatting.BOLD));
        //getBlockEntity().getLevel().sendBlockUpdated(getBlockEntity().getBlockPos(), getBlockEntity().getBlockState(), getBlockEntity().getBlockState(), 3);
    }
}
