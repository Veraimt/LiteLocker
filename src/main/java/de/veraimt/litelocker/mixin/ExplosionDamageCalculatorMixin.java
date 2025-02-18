package de.veraimt.litelocker.mixin;


import de.veraimt.litelocker.utils.AccessChecker;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.EntityBasedExplosionDamageCalculator;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ExplosionDamageCalculator.class, EntityBasedExplosionDamageCalculator.class})
public class ExplosionDamageCalculatorMixin {

    @Inject(method = "shouldBlockExplode", at = @At("RETURN"), cancellable = true)
    public void shouldBlockExplode(Explosion explosion, BlockGetter blockGetter, BlockPos blockPos,
                                   BlockState blockState, float f, CallbackInfoReturnable<Boolean> cir) {
        if(!AccessChecker.canAccess(blockGetter, blockPos, null)) {
            cir.setReturnValue(false);
        }
    }
}
