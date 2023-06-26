package de.veraimt.litelocker.protection.protector.autofill;

import de.veraimt.litelocker.protection.protectable.ProtectableContainer;
import de.veraimt.litelocker.signs.ProtectorSign;
import net.minecraft.server.network.FilteredText;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Arrays;
import java.util.List;

public class SignManager {


    private final ProtectorSign protectorSign;

    public SignManager(ProtectorSign protectorSign) {
        this.protectorSign = protectorSign;
    }

    public boolean isValid() {
        return protectorSign.isAttachedContainerValid();
    }

    public boolean tryAutoFill(Player player) {
        var signBlockEntity = protectorSign.getBlockEntity();
        BlockEntity blockEntity =
                signBlockEntity.getLevel().getBlockEntity(protectorSign.getAttachedBlock().blockPos());

        if (!(blockEntity instanceof  ProtectableContainer container)) {
            return false;
        }

        if (container.hasProtector()) {
            return false;
        } else {
            signBlockEntity.setAllowedPlayerEditor(player.getUUID());
            signBlockEntity.updateSignText(player, true, List.of(
                    FilteredText.passThrough(ProtectorSign.Tag.PRIVATE.tag),
                    FilteredText.passThrough(player.getName().getString())
            ));
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            return true;
        }
    }

    public void activate() {
        protectorSign.activate();
        BlockEntity blockEntity = protectorSign.getBlockEntity();
        blockEntity.getLevel().sendBlockUpdated(blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity.getBlockState(), 3);
    }
}
