package de.veraimt.litelocker.mixin;

import de.veraimt.litelocker.utils.AccessChecker;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class PlayerMixin {

    /**
     * @see net.minecraft.server.level.ServerPlayerGameMode#destroyBlock(BlockPos)
     */
    @Inject(method = "blockActionRestricted", at = @At("HEAD"), cancellable = true)
    public void blockActionRestricted(Level level, BlockPos blockPos, GameType $$2, CallbackInfoReturnable<Boolean> cir) {

        if(!AccessChecker.canAccess(level, blockPos, player())) {
            var blockState = level.getBlockState(blockPos);

            level.sendBlockUpdated(blockPos, blockState, blockState, 3);

            cir.setReturnValue(true);
        }
    }

    private Player player() {
        return (Player) (Object) this;
    }
}
