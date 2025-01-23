package de.veraimt.litelocker.protection;

import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public interface Protectable {
    /**
     * Checks if the given Player can access this Protectable
     * @param player
     * @return true if the Player is not null and can access this Protectable, false otherwise
     */
    boolean canAccess(@Nullable Player player);
}
