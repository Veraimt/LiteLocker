package de.veraimt.litelocker.mixin;

import de.veraimt.litelocker.signs.ProtectorSign;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SignBlock.class)
public class SignBlockMixin {

    @Inject(method = "openTextEdit", at = @At("HEAD"), cancellable = true)
    public void openEditText(Player player, SignBlockEntity signBlockEntity, boolean frontSide, CallbackInfo ci) {
        if (!frontSide && signBlockEntity instanceof ProtectorSign)
            ci.cancel();
    }
}
