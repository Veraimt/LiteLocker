package de.veraimt.litelocker.protection.protectable;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;

public class ProtectableBlockRegistry {
    public static final ProtectableBlockRegistry INSTANCE = new ProtectableBlockRegistry();
    private final List<Block> excludedBlocks;

    private ProtectableBlockRegistry() {
        this.excludedBlocks = new LinkedList<>();
        //TODO load from config
    }

    public boolean isExcluded(Block block) {
        return excludedBlocks.contains(block);
    }

    public List<Block> getExcludedBlocks() {
        return excludedBlocks;
    }

    @Deprecated(forRemoval = true)
    @SuppressWarnings("unchecked")
    public Class<? extends BlockBehaviour>[] getProtectableBlocksClasses(@Nullable Class<? extends BlockBehaviour> clazz) {
        int arraySize = excludedBlocks.size();
        if (clazz != null) {
            arraySize++;
        }

        var classes = new Class[arraySize];
        for (int i = 0; i < excludedBlocks.size(); i++) {
            classes[i] = excludedBlocks.get(i).getClass();
        }

        if (clazz != null) {
            classes[arraySize-1] = clazz;
        }

        return (Class<? extends BlockBehaviour>[]) classes;
    }
}
