package de.veraimt.litelocker.protection;

import de.veraimt.litelocker.mixin.containers.base.BaseContainerBlockEntityMixin;
import de.veraimt.litelocker.utils.BlockEntityProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;

import java.util.UUID;

public interface ProtectableBlockContainer extends ProtectableContainer, Protection.Access, BlockEntityProvider<BaseContainerBlockEntity> {

    default void addProtector(Protector<?> protector) {
        get().addProtector(protector);
    }

    default void removeProtector(Protector<?> protector) {
        get().removeProtector(protector);
        if (protector.isMain())
            get().protectors.stream().findAny().ifPresent(Protector::setMain);
    }

    default boolean hasProtector() {
        return get().hasProtector();
    }

    default boolean hasProtector(Protector<?> protector) {
        return get().hasProtector(protector);
    }

    default boolean hasUser(UUID playerUUID) {
        return get().hasUser(playerUUID);
    }

}
