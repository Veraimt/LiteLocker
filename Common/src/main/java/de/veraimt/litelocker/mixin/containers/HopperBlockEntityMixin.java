package de.veraimt.litelocker.mixin.containers;

import net.minecraft.world.Container;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.Hopper;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HopperBlockEntity.class)
public class HopperBlockEntityMixin {

    @Inject(method = "suckInItems", at = @At("HEAD"))
    private static void suckInItems(Level $$0, Hopper $$1, CallbackInfoReturnable<Boolean> cir) {
        System.out.println("Hopper suck");
    }

    @Inject(method = "getSourceContainer", at = @At("RETURN"))
    private static void getSourceContainer(Level $$0, Hopper $$1, CallbackInfoReturnable<Container> cir) {
        long startNanos = System.nanoTime();
        System.out.println(cir.getReturnValue());
        long endNanos = System.nanoTime() - startNanos;
        System.out.println("took " + endNanos + "ns / " + (endNanos/1000) + "ms");
    }
}
