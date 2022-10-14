package de.veraimt.litelocker.mixin.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractChestBlock;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Supplier;

@Deprecated(forRemoval = true)
@Mixin(ChestBlock.class)
public abstract class ChestBlockMixin extends AbstractChestBlock<ChestBlockEntity> {

    private static final DoubleBlockCombiner.Combiner<ChestBlockEntity, Optional<Container>> CHEST_COMBINER;
    static {
        CHEST_COMBINER = new DoubleBlockCombiner.Combiner<ChestBlockEntity, Optional<Container>>() {
            public Optional<Container> acceptDouble(ChestBlockEntity $$0, ChestBlockEntity $$1) {
                return Optional.of(new CompoundContainer($$0, $$1));
            }

            public Optional<Container> acceptSingle(ChestBlockEntity $$0) {
                return Optional.of($$0);
            }

            public Optional<Container> acceptNone() {
                return Optional.empty();
            }
        };
    }
    protected ChestBlockMixin(Properties $$0, Supplier<BlockEntityType<? extends ChestBlockEntity>> $$1) {
        super($$0, $$1);
    }

    @Inject(method = "getBlockType", at = @At("RETURN"))
    private static void getBlockType(BlockState $$0, CallbackInfoReturnable<DoubleBlockCombiner.BlockType> cir) {
        /*
        System.out.println("#Called ChestBlock#getBlockType");
        System.out.println("#RESULT " + cir.getReturnValue());
        System.out.println("STACKTRACE " + Arrays.toString(Thread.currentThread().getStackTrace()));

         */
    }

    @Deprecated(forRemoval = true)
    @Inject(method = "getContainer", at = @At("HEAD"), cancellable = true)
    private static void getContainer(ChestBlock $$0, BlockState $$1, Level $$2, BlockPos $$3, boolean $$4, CallbackInfoReturnable<Container> cir) {
        //TODO REMOVE
        if (true)
            return;
        System.out.println("#Called ChestBlock#getContainer");

        long startNanos = System.nanoTime();
        cir.setReturnValue((Container)((Optional)$$0.combine($$1, $$2, $$3, $$4).apply(CHEST_COMBINER)).orElse((Object)null));
        System.out.println(cir.getReturnValue());
        long endNanos = System.nanoTime() - startNanos;
        System.out.println("§ Update took " + endNanos + "ns / " + (endNanos/1000) + "ms");
        /*
        System.out.println("#Called ChestBlock#getBlockType");
        System.out.println("#RESULT " + cir.getReturnValue());
        System.out.println("STACKTRACE " + Arrays.toString(Thread.currentThread().getStackTrace()));

         */
    }
}
