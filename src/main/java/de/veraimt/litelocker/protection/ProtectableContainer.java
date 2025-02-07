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

    @Override
    default boolean canAccess(@Nullable Player player) {
        if (!hasProtector()) //has no protector, access granted
            return true;

        UUID playerUUID = player == null ? null : player.getUUID();
        boolean anyValid = false;
        for (var protector : protectors()) {
            if (!protector.isValid())
                continue;
            anyValid = true;
            if (protector.canAccess(playerUUID))
                return true;
        }
        return !anyValid;
    }
}