package de.veraimt.litelocker.mixin.containers;

import de.veraimt.litelocker.protection.ProtectableContainer;
import de.veraimt.litelocker.protection.Protector;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

import java.util.HashSet;
import java.util.Set;

@Mixin(ChestBlockEntity.class)
public abstract class ChestBlockEntityMixin extends RandomizableContainerBlockEntityMixin {
    protected ChestBlockEntityMixin(BlockEntityType<?> $$0, BlockPos $$1, BlockState $$2) {
        super($$0, $$1, $$2);
    }

    @Override
    public Set<Protector<?>> combinedProtectors() {
        if (getBlockState().getBlock() instanceof ChestBlock chestBlock) {
            return chestBlock.combine(getBlockState(), getLevel(), getBlockPos(), false).apply(
                    new DoubleBlockCombiner.Combiner<ChestBlockEntity, Set<Protector<?>>>() {
                        @Override
                        public Set<Protector<?>> acceptDouble(ChestBlockEntity chestBlockEntity, ChestBlockEntity s1) {
                            var protectors = new HashSet<>(((ProtectableContainer) chestBlockEntity).protectors());
                            protectors.addAll(((ProtectableContainer) s1).protectors());
                            return protectors;
                        }

                        @Override
                        public Set<Protector<?>> acceptSingle(ChestBlockEntity chestBlockEntity) {
                            return ((ProtectableContainer) chestBlockEntity).protectors();
                        }

                        @Override
                        public Set<Protector<?>> acceptNone() {
                            return null;
                        }
                    });
        }
        return null;
    }
}
