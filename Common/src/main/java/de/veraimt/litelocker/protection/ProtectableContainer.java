package de.veraimt.litelocker.protection;

import de.veraimt.litelocker.utils.BlockEntityProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.*;

public interface ProtectableContainer extends BlockEntityProvider<BaseContainerBlockEntity> {

    default void addProtector(Protector<?> protector) {
        protectors().add(protector);
    }

    default void removeProtector(Protector<?> protector) {
        protectors().remove(protector);
        combinedProtectors().stream().findFirst().ifPresent(Protector::setMain);
    }

    default Set<Protector<?>> findProtectors() {
        var blockEntity = getBlockEntity();

        Level world = blockEntity.getLevel();
        Objects.requireNonNull(world);
        Set<Protector<?>> list = new HashSet<>();

        for (var direction : Direction.values()) {
            BlockEntity be = world.getBlockEntity(blockEntity.getBlockPos().relative(direction));
            if (be instanceof Protector<?> p) {
                if (p.isValid() && p.getAttachedContainer() == this)
                    list.add(p);
            }
        }
        return list;
    }

    default Set<Protector<?>> combinedProtectors() {
        return protectors();
    }

    Set<Protector<?>> protectors();

    default boolean hasProtector() {
        return !combinedProtectors().isEmpty();
    }

    default boolean hasProtector(Protector<?> protector) {
        return combinedProtectors().contains(protector);
    }

    default boolean hasUser(UUID playerUUID) {
        for (var p : combinedProtectors()) {
            if (p.hasUser(playerUUID)) {
                return true;
            }
        }
        return false;
    }

    default boolean canAccess(Player player) {
        if (!hasProtector())
            return true;

        return hasUser(player.getUUID());
    }
}