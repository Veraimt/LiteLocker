package de.veraimt.litelocker.utils;

import net.minecraft.world.level.block.entity.BlockEntity;

public interface BlockEntityProvider<T extends BlockEntity> {
    /**
     *
     * @return the {@link BlockEntity} associated with this object
     */
    T getBlockEntity();
}
