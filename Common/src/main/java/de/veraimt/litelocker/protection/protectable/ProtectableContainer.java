package de.veraimt.litelocker.protection.protectable;

import de.veraimt.litelocker.protection.protector.Protector;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.util.UUID;

public interface ProtectableContainer extends Protectable {

    void removeProtector(Protector<?> protector);

    boolean hasProtector();

    boolean hasProtector(Protector<?> protector);

    boolean hasUser(UUID playerUUID);

    @Override
    default boolean canAccess(@Nullable Player player) {
        if (!hasProtector())
            return true;

        return hasUser(player == null ? null : player.getUUID());
    }
}