package de.veraimt.litelocker.mixin.test;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.OptionalInt;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {

    @Deprecated
    @Inject(method = "openTextEdit", at = @At("HEAD"))
    public void openTextEdit(SignBlockEntity signBlockEntity, CallbackInfo ci) {
        System.out.println("openEditText");


    }

    @Inject(method = "openMenu", at = @At("HEAD"))
    public void openMenu(MenuProvider $$0, CallbackInfoReturnable<OptionalInt> cir) {
        System.out.println("OPEN MENU");
        System.out.println("STACKTRACE" + Arrays.toString(Thread.currentThread().getStackTrace()));
    }
}
