package de.veraimt.litelocker.protection;

import net.minecraft.world.level.block.entity.ChestBlockEntity;

import java.util.Set;

public interface ProtectableChest extends ProtectableContainer {
    void setCombinedProtectorsCache(Set<Protector<?>> protectors);
}
