package de.veraimt.litelocker.protection;

import de.veraimt.litelocker.utils.BlockEntityProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public interface ProtectableContainer extends Protectable, BlockEntityProvider<BaseContainerBlockEntity> {

    List<Protector<?>> protectors();

    default boolean hasProtector() {
        return protectors() != null && !protectors().isEmpty();
    }

    default boolean hasProtector(Protector<?> protector) {
        return protectors().contains(protector);
    }

    /**
     * Checks every {@link Protector} if it has the player with the given UUID
     * @param playerUUID UUID of the Player
     * @return is this ProtectableContainer has the Player with the given UUID
     */
    default boolean hasUser(UUID playerUUID) {
        for (var protector : protectors()) {
            if (protector.hasUser(playerUUID))
                return true;
        }
        return false;
    }

    @Override
    default boolean canAccess(@Nullable Player player) {
        if (!hasProtector()) //has no protector, access granted
            return true;

        return hasUser(player == null ? null : player.getUUID()); //check if this container has the user
    }
}