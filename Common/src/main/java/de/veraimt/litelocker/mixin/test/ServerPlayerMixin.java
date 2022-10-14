package de.veraimt.litelocker.mixin.test;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
@Deprecated(forRemoval = true)
//TODO Class only for Debug
public class ServerPlayerMixin {

    @Inject(method = "openTextEdit", at = @At("HEAD"), cancellable = true)
    public void openTextEdit(SignBlockEntity signBlockEntity, CallbackInfo ci) {
        System.out.println("openEditText");


    }
}
