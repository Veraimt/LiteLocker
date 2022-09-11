package de.veraimt.litelocker.mixin;

import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;

@Mixin(ChestBlock.class)
public class ChestBlockMixin {
    @Inject(method = "getBlockType", at = @At("RETURN"))
    private static void getBlockType(BlockState $$0, CallbackInfoReturnable<DoubleBlockCombiner.BlockType> cir) {
        System.out.println("#Called ChestBlock#getBlockType");
        System.out.println("#RESULT " + cir.getReturnValue());
        System.out.println("STACKTRACE " + Arrays.toString(Thread.currentThread().getStackTrace()));
    }
}
