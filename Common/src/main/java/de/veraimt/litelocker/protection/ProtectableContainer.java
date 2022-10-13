package de.veraimt.litelocker.protection;

import de.veraimt.litelocker.utils.BlockEntityProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.*;

public interface ProtectableContainer {

    void addProtector(Protector<?> protector);

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