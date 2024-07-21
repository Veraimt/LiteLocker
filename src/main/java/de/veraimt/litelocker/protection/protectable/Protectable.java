package de.veraimt.litelocker.protection.protectable;

import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;


public interface Protectable {
    boolean canAccess(@Nullable Player player);
}
