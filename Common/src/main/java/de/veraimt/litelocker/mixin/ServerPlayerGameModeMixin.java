package de.veraimt.litelocker.mixin;

import de.veraimt.litelocker.protection.protector.ProtectorItem;
import de.veraimt.litelocker.utils.AccessChecker;
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

@Mixin(ServerPlayerGameMode.class)
public class ServerPlayerGameModeMixin {
    @Inject(method = "useItemOn", at = @At("HEAD"), cancellable = true)
    public void useItemOn(ServerPlayer player, Level level, ItemStack itemStack, InteractionHand $$3,
                          BlockHitResult blockHitResult, CallbackInfoReturnable<InteractionResult> cir) {
        //Called when Player clicks with item on block
        System.out.println("Method Call: "+ getClass().getName() +"#useItemOn");

        if (itemStack.getItem() instanceof ProtectorItem)
            if (!AccessChecker.canAccess(level, blockHitResult.getBlockPos(), player))
                cir.setReturnValue(InteractionResult.SUCCESS);



        //NEXT: ItemStack#useOn
    }
}
