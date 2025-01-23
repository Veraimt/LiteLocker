package de.veraimt.litelocker.mixin.signs;

import com.llamalad7.mixinextras.sugar.Local;
import de.veraimt.litelocker.utils.AccessChecker;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SignBlock.class)
public class SignBlockMixin {

    @Inject(method = "useWithoutItem",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/entity/SignBlockEntity;isFacingFrontText(Lnet/minecraft/world/entity/player/Player;)Z",
                    shift = At.Shift.BEFORE),
            cancellable = true
    )
    private void useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult,
                                CallbackInfoReturnable<InteractionResult> cir, @Local SignBlockEntity signBlockEntity
    ) {
        if (!AccessChecker.canAccess(signBlockEntity, player)) {
            //Block Player action
            cir.setReturnValue(InteractionResult.SUCCESS);
            cir.cancel();
            player.playNotifySound(SoundEvents.WAXED_SIGN_INTERACT_FAIL, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }
}
