package de.veraimt.litelocker.mixin.test;

import de.veraimt.litelocker.protection.protectable.Protectable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.Arrays;

@Deprecated(forRemoval = true)
@Mixin(Level.class)
public abstract class LevelMixin {

    @Shadow @Nullable public abstract BlockEntity getBlockEntity(BlockPos $$0);

    @Shadow public abstract void setBlocksDirty(BlockPos $$0, BlockState $$1, BlockState $$2);

    @Shadow public abstract BlockState getBlockState(BlockPos $$0);

    @Shadow public abstract void sendBlockUpdated(BlockPos blockPos, BlockState blockState, BlockState blockState1, int i);

    @Inject(method = "setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;II)Z",
    at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;getChunkAt(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/chunk/LevelChunk;"),
    cancellable = true)
    public void preventBlockDestruction(BlockPos blockPos, BlockState blockState, int $$2, int $$3, CallbackInfoReturnable<Boolean> cir) {
        if (true)
            return;


        var blockEntity = getBlockEntity(blockPos);
        if (blockEntity instanceof Protectable protectable) {
            if (!protectable.canAccess(null)) {
                System.out.println("Cannot access");
                System.out.println("STACKTRACE" + Arrays.toString(Thread.currentThread().getStackTrace()));
                sendBlockUpdated(blockPos, getBlockState(blockPos), getBlockState(blockPos), 3);
                cir.setReturnValue(false);
            }
        }
    }

    @Inject(method = "removeBlock", at = @At("HEAD"), cancellable = true)
    public void removeBlock(BlockPos blockPos, boolean $$1, CallbackInfoReturnable<Boolean> cir) {


        if (true)
            return;


        var blockEntity = getBlockEntity(blockPos);
        if (blockEntity instanceof Protectable protectable) {
            if (!protectable.canAccess(null)) {
                System.out.println("Cannot access");
                System.out.println("STACKTRACE" + Arrays.toString(Thread.currentThread().getStackTrace()));
                sendBlockUpdated(blockPos, getBlockState(blockPos), getBlockState(blockPos), 3);
                cir.setReturnValue(false);
            }
        }
    }

}
