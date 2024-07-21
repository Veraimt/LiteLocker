package de.veraimt.litelocker.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public record BlockPosState(BlockPos blockPos, BlockState blockState) {

    public static BlockPosState fromBlockEntity(BlockEntity blockEntity) {
        return new BlockPosState(blockEntity.getBlockPos(), blockEntity.getBlockState());
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BlockPosState bs))
            return false;

        return bs.blockState.equals(this.blockState) && bs.blockPos.equals(this.blockPos);
    }

    @Override
    public String toString() {
        return "{" + blockPos +
                ", " + blockState +
                '}';
    }
}
