package de.veraimt.litelocker.mixin;

import de.veraimt.litelocker.LiteLocker;
import net.minecraft.client.gui.screens.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Deprecated(forRemoval = true)
@Mixin(TitleScreen.class)
public class TestMixin {

    @Inject(at = @At("HEAD"), method = "init()V")
    private void init(CallbackInfo info) {
        LiteLocker.LOGGER.info("Hello from Mixin");
    }
}
