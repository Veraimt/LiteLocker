package de.veraimt.litelocker.utils;

import de.veraimt.litelocker.protection.Protectable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;

public class AccessChecker {

    public static boolean canAccess(BlockGetter levelLike, BlockPos blockPos, Player player) {
        var blockEntity = levelLike.getBlockEntity(blockPos);
        if (blockEntity instanceof Protectable protectable) {
            return protectable.canAccess(player);
        }
        return true;
    }
}
