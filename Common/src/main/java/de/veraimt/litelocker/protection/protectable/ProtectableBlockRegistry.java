package de.veraimt.litelocker.protection.protectable;

import de.veraimt.litelocker.LiteLocker;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.LinkedList;
import java.util.List;

public class ProtectableBlockRegistry {
    public static final ProtectableBlockRegistry INSTANCE = new ProtectableBlockRegistry();
    private final List<Block> excludedBlocks;

    private ProtectableBlockRegistry() {
        this.excludedBlocks = new LinkedList<>();
        var excludedBlockNames = LiteLocker.config.getExclude();
        for (var blockName : excludedBlockNames) {
            //EXAMPLE: "minecraft:grass_block"
            //FIXME
            //excludedBlocks.add(Registry.BLOCK.get(ResourceLocation.tryParse(blockName)));
        }
    }

    public boolean isExcluded(Block block) {
        return excludedBlocks.contains(block);
    }

    public List<Block> getExcludedBlocks() {
        return excludedBlocks;
    }

}
