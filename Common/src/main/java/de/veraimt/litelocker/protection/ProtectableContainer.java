package de.veraimt.litelocker.protection;

import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public interface ProtectableContainer {

    void removeProtector(Protector<?> protector);

    boolean hasProtector();

    boolean hasProtector(Protector<?> protector);

    boolean hasUser(UUID playerUUID);

    default boolean canAccess(Player player) {
        if (!hasProtector())
            return true;

        return hasUser(player.getUUID());
    }
}