package de.veraimt.litelocker.protection.protectable;

import de.veraimt.litelocker.protection.Protection;
import de.veraimt.litelocker.protection.protector.Protector;
import de.veraimt.litelocker.utils.BlockEntityProvider;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;

import java.util.UUID;

public interface ProtectableBlockContainer extends ProtectableContainer, Protection.Access, BlockEntityProvider<BaseContainerBlockEntity> {

    default void addProtector(Protector<?> protector) {
        get().addProtector(protector);
    }

    default void removeProtector(Protector<?> protector) {
        var protection = getDirect();
        if (protection == null)
            return;
        protection.removeProtector(protector);
        if (protector.isMain())
            protection.protectors().stream().findAny().ifPresent(Protector::setMain);
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
