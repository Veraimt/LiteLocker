package de.veraimt.litelocker.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Deprecated(forRemoval = true)
@Mixin(ServerPlayerGameMode.class)
public class ServerPlayerGameModeMixin {
    @Inject(method = "useItemOn", at = @At("HEAD"))
    public void useItemOn(ServerPlayer $$0, Level $$1, ItemStack $$2, InteractionHand $$3, BlockHitResult $$4, CallbackInfoReturnable<InteractionResult> cir) {
        //Called when Player clicks with item on block
        System.out.println("Method Call: "+ getClass().getName() +"#useItemOn");
        //NEXT: ItemStack#useOn
    }

    @Inject(method = "useItem", at = @At("HEAD"))
    public void useItem(ServerPlayer $$0, Level $$1, ItemStack $$2, InteractionHand $$3, CallbackInfoReturnable<InteractionResult> cir) {
        System.out.println("Method Call: "+ getClass().getName() +"#useItem");
    }
}
