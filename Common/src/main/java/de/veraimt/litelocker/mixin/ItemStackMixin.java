package de.veraimt.litelocker.mixin;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Deprecated(forRemoval = true)
@Mixin(ItemStack.class)
public class ItemStackMixin {

    @Inject(method = "useOn", at = @At("HEAD"))
    public void useItemOn(UseOnContext $$0, CallbackInfoReturnable<InteractionResult> cir) {
        //PREV: ServerPlayerGameMode#useItemOn
        //Called when Player uses this item on block
        System.out.println("Method Call: "+ getClass().getName() +"#useOn");
        //NEXT:
    }
}
