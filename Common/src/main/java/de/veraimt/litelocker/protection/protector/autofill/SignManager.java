package de.veraimt.litelocker.protection.protector.autofill;

import de.veraimt.litelocker.protection.protectable.ProtectableContainer;
import de.veraimt.litelocker.protection.protector.ProtectorSign;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class SignManager {


    private final ProtectorSign protectorSign;

    public SignManager(ProtectorSign protectorSign) {
        this.protectorSign = protectorSign;
    }

    public boolean isValid() {
        return protectorSign.isAttachedContainerValid();
    }

    public boolean tryAutoFill(Player player) {
        BlockEntity blockEntity =
                protectorSign.getBlockEntity().getLevel().getBlockEntity(protectorSign.getAttachedBlock().blockPos());

        if (!(blockEntity instanceof  ProtectableContainer container)) {
            return false;
        }

        if (container.hasProtector()) {
            return false;
        } else {
            protectorSign.getBlockEntity().setMessage(0, new TextComponent(ProtectorSign.Tag.PRIVATE.tag));
            protectorSign.getBlockEntity().setMessage(1, player.getName());
            System.out.println("Auto filled");
            return true;
        }
    }

    public void activate() {
        protectorSign.activate();
        BlockEntity blockEntity = protectorSign.getBlockEntity();
        blockEntity.getLevel().sendBlockUpdated(blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity.getBlockState(), 3);
    }
}
