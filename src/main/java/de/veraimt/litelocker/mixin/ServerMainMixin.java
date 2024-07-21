package de.veraimt.litelocker.mixin;

import de.veraimt.litelocker.LiteLocker;
import net.minecraft.server.Main;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Function;

@Mixin(Main.class)
public class ServerMainMixin {

    @Redirect(method = "main", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/MinecraftServer;spin(Ljava/util/function/Function;)Lnet/minecraft/server/MinecraftServer;")
    )
    private static <S extends MinecraftServer> S a(Function<Thread, S> function) {
        var server = MinecraftServer.spin(function);
        LiteLocker.server = server;
        return server;
    }
}
