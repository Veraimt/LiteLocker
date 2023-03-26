package de.veraimt.litelocker.mixin;

import de.veraimt.litelocker.protection.protector.ProtectorItem;
import de.veraimt.litelocker.utils.AccessChecker;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerGameMode.class)
public class ServerPlayerGameModeMixin {
    @Inject(method = "useItemOn", at = @At("HEAD"), cancellable = true)
    public void useItemOn(ServerPlayer player, Level level, ItemStack itemStack, InteractionHand $$3,
                          BlockHitResult blockHitResult, CallbackInfoReturnable<InteractionResult> cir) {

        if (player.gameMode.getGameModeForPlayer() == GameType.SPECTATOR) {
            if (!AccessChecker.canAccess(level, blockHitResult.getBlockPos(), player)) {
                cir.setReturnValue(InteractionResult.SUCCESS);
            }
            return;
        }


        //Called when Player clicks with item on block
        if (itemStack.getItem() instanceof ProtectorItem)
            if (!AccessChecker.canAccess(level, blockHitResult.getBlockPos(), player))
                cir.setReturnValue(InteractionResult.SUCCESS);


    }


    //if this Method returns false, then use will be called on the targeted block (interacting with targeted block)
    @ModifyVariable(method = "useItemOn", at = @At("STORE"), ordinal = 1)
    public boolean modifyUseBlockOrUseItem(boolean value,
                        ServerPlayer player, Level level, ItemStack $$2, InteractionHand $$3, BlockHitResult blockHitResult) {
        //if the Player has an ProtectorItem in his hand and the targeted block is lockable
        //then true is returned, so that the Player won't interact with the targeted block
        //but instead place the ProtectorItem
        if ($$2.getItem() instanceof ProtectorItem) {
            if (AccessChecker.canLock(level, blockHitResult.getBlockPos(), player))
                return true;

        }

        return value;
    }

    @Redirect(method = "useItemOn", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/state/BlockState;use(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/phys/BlockHitResult;)Lnet/minecraft/world/InteractionResult;"))
    public InteractionResult allowUse(BlockState instance, Level level, Player player,
                                      InteractionHand interactionHand, BlockHitResult blockHitResult) {
        //Consume Action, if Player cannot access block
        if (!AccessChecker.canAccess(level, blockHitResult.getBlockPos(), player)) {
            return InteractionResult.CONSUME;
        }
        //Normal Behavior
        return instance.use(level, player, interactionHand, blockHitResult);
    }
}
