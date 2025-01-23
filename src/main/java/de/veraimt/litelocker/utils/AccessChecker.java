package de.veraimt.litelocker.utils;

import de.veraimt.litelocker.protection.Protectable;
import de.veraimt.litelocker.protection.ProtectableContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;

public final class AccessChecker {

    /* Utility Class, no instances allowed */
    private AccessChecker() {}

    /**
     * Returns true if the given player can access the BlockEntity provided by the given BlockGetter
     * at the given BlockPos
     * @param blockGetter for retrieving a BlockEntity at the given BlockPos
     * @param blockPos The position of the Block to check access for, cannot be null
     * @param player The Player to check access for, if null return false
     * @return true if the Player can access the Block, false otherwise
     */
    public static boolean canAccess(BlockGetter blockGetter, BlockPos blockPos, Player player) {
        return canAccess(blockGetter.getBlockEntity(blockPos), player);
    }

    /**
     * Returns true if the given player can access the given BlockEntity
     * @param blockEntity The BlockEntity to be accessed
     * @param player The Player to check access for, if null return false
     * @return true if the Player can access the Block, false otherwise
     */
    public static boolean canAccess(BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof Protectable protectable) {
            return protectable.canAccess(player);
        }
        return true;
    }

    public static boolean canLock(BlockGetter levelLike, BlockPos blockPos, Player player) {
        var blockEntity = levelLike.getBlockEntity(blockPos);
        if (blockEntity instanceof ProtectableContainer container) {
            return container.canAccess(player) && !container.hasProtector();
        }
        return false;
    }
}
