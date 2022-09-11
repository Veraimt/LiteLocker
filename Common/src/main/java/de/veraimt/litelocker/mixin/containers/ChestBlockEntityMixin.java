package de.veraimt.litelocker.mixin.containers;

import de.veraimt.litelocker.protection.ProtectableChest;
import de.veraimt.litelocker.protection.ProtectableContainer;
import de.veraimt.litelocker.protection.Protector;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Set;

@Mixin(ChestBlockEntity.class)
public abstract class ChestBlockEntityMixin extends RandomizableContainerBlockEntityMixin implements ProtectableChest {
    protected ChestBlockEntityMixin(BlockEntityType<?> $$0, BlockPos $$1, BlockState $$2) {
        super($$0, $$1, $$2);
    }

    @Override
    public Set<Protector<?>> protectors() {/*
        if (getBlockState().getBlock() instanceof ChestBlock chestBlock) {
            Set<Protector<?>> protectors = chestBlock.combine(getBlockState(), getLevel(), getBlockPos(), false).apply(
                    new DoubleBlockCombiner.Combiner<ChestBlockEntity, Set<Protector<?>>>() {
                        @Override
                        public Set<Protector<?>> acceptDouble(ChestBlockEntity chestBlockEntity, ChestBlockEntity s1) {
                            return ((ProtectableContainer) chestBlockEntity).protectors();
                        }

                        @Override
                        public Set<Protector<?>> acceptSingle(ChestBlockEntity chestBlockEntity) {
                            return null;
                        }

                        @Override
                        public Set<Protector<?>> acceptNone() {
                            return null;
                        }
                    });
            return protectors;
        }
        */
        return null;
    }

    @Override
    public BaseContainerBlockEntity getBlockEntity() {
        return this;
    }
}
