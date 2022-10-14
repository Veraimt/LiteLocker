package de.veraimt.litelocker.protection;

import de.veraimt.litelocker.utils.MixinAccessible;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class Protection {

    public Protection() {
    }

    private Protection(Set<Protector<?>> protectors) {
        this.protectors = protectors;
    }

    Set<Protector<?>> protectors = new HashSet<>();

    public void addProtector(Protector<?> protector) {
        protectors.add(protector);
    }

    public boolean removeProtector(Protector<?> protector) {
        return protectors.remove(protector);
    }

    public boolean hasProtector() {
        return !protectors.isEmpty();
    }

    public boolean hasProtector(Protector<?> protector) {
        return protectors.contains(protector);
    }

    public boolean hasUser(UUID playerUUID) {
        for (var p : protectors) {
            if (p.hasUser(playerUUID)) {
                return true;
            }
        }
        return false;
    }

    public Set<Protector<?>> protectors() {
        return protectors;
    }

    public boolean canAccess(Player player) {
        if (!hasProtector())
            return true;

        return hasUser(player.getUUID());
    }

    public static Protection find(ProtectableBlockContainer container) {
        return new Protection(findProtectors(container));
    }

    private static Set<Protector<?>> findProtectors(ProtectableBlockContainer protectableContainer) {
        var blockEntity = protectableContainer.getBlockEntity();

        Level world = blockEntity.getLevel();
        Objects.requireNonNull(world);
        Set<Protector<?>> list = new HashSet<>();

        for (var direction : Direction.values()) {
            BlockEntity be = world.getBlockEntity(blockEntity.getBlockPos().relative(direction));
            if (be instanceof Protector<?> p) {
                if (p.isValid() && p.getAttachedContainer() == protectableContainer)
                    list.add(p);
            }
        }
        return list;

    }

    public interface Access extends MixinAccessible<Protection> {}
}
