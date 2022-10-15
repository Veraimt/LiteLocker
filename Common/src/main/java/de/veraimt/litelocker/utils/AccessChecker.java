package de.veraimt.litelocker.utils;

import de.veraimt.litelocker.protection.protectable.Protectable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;

public class AccessChecker {

    public static boolean canAccess(BlockGetter levelLike, BlockPos blockPos, Player player) {
        return canAccess(levelLike.getBlockEntity(blockPos), player);
    }

    public static boolean canAccess(BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof Protectable protectable) {
            return protectable.canAccess(player);
        }
        return true;
    }
}
