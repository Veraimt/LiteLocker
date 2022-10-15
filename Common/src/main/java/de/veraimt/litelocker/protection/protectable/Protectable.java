package de.veraimt.litelocker.protection.protectable;

import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;

public interface Protectable {
    boolean canAccess(@Nullable Player player);
}
