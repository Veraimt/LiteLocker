package de.veraimt.litelocker.mixin.containers;

import de.veraimt.litelocker.mixin.containers.base.RandomizableContainerBlockEntityMixin;
import de.veraimt.litelocker.protection.ProtectableChest;
import de.veraimt.litelocker.protection.ProtectableContainer;
import de.veraimt.litelocker.protection.Protector;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

import java.util.HashSet;
import java.util.Set;

@Mixin(ChestBlockEntity.class)
public abstract class ChestBlockEntityMixin extends RandomizableContainerBlockEntityMixin implements ProtectableChest {
    private Set<Protector<?>> combinedProtectorsCached;

    private static final DoubleBlockCombiner.Combiner<ChestBlockEntity, Boolean> HAS_PROTECTOR_COMBINER =
            new DoubleBlockCombiner.Combiner<>() {
                @Override
                public @NotNull Boolean acceptDouble(@NotNull ChestBlockEntity chestBlockEntity, @NotNull ChestBlockEntity s1) {
                    return ((ProtectableContainer) chestBlockEntity).hasProtector() && ((ProtectableChest) s1).hasProtector();
                }

                @Override
                public @NotNull Boolean acceptSingle(@NotNull ChestBlockEntity chestBlockEntity) {
                    return ((ProtectableContainer) chestBlockEntity).hasProtector();
                }

                @Override
                public @NotNull Boolean acceptNone() {
                    return false;
                }
            };

    private static final DoubleBlockCombiner.Combiner<ChestBlockEntity, Set<Protector<?>>> PROTECTOR_COMBINER =
            new DoubleBlockCombiner.Combiner<>() {
                @Override
                public @NotNull Set<Protector<?>> acceptDouble(@NotNull ChestBlockEntity chestBlockEntity, @NotNull ChestBlockEntity s1) {
                    var protectors = new HashSet<>(((ProtectableContainer) chestBlockEntity).protectors());
                    var otherChest = (ProtectableChest) s1;
                    protectors.addAll(otherChest.protectors());
                    otherChest.setCombinedProtectorsCache(protectors);
                    return protectors;
                }

                @Override
                public @NotNull Set<Protector<?>> acceptSingle(@NotNull ChestBlockEntity chestBlockEntity) {
                    return ((ProtectableContainer) chestBlockEntity).protectors();
                }

                @Override
                public @NotNull Set<Protector<?>> acceptNone() {
                    return new HashSet<>();
                }
            };

    protected ChestBlockEntityMixin(BlockEntityType<?> $$0, BlockPos $$1, BlockState $$2) {
        super($$0, $$1, $$2);
    }

    @Override
    public Set<Protector<?>> combinedProtectors() {
        updateCombinedProtectors();
        if (combinedProtectorsCached == null)
            updateCombinedProtectors();
        return combinedProtectorsCached;
    }

    private void updateCombinedProtectors() {
        long startNanos = System.nanoTime();
        System.out.println("§ before: " + combinedProtectorsCached);

        combinedProtectorsCached = combine(PROTECTOR_COMBINER);

        System.out.println("§ after: " + combinedProtectorsCached);
        long endNanos = System.nanoTime() - startNanos;
        System.out.println("§ Update took " + endNanos + "ns / " + (endNanos/1000) + "ms");
    }

    private ChestBlock getChestBlock() {
        return (ChestBlock) getBlockState().getBlock();
    }

    private <T> T combine(DoubleBlockCombiner.Combiner<ChestBlockEntity, T> combiner) {
        return getChestBlock().combine(getBlockState(), getLevel(), getBlockPos(), false).apply(combiner);
    }

    @Override
    public boolean hasProtector() {
        return combine(HAS_PROTECTOR_COMBINER);
    }

    @Override
    public void onChanged() {
        super.onChanged();
        updateCombinedProtectors();
    }

    @Override
    public void setCombinedProtectorsCache(Set<Protector<?>> protectors) {
        combinedProtectorsCached = protectors;
    }
}
