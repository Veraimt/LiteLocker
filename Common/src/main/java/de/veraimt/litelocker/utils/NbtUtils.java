package de.veraimt.litelocker.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;

public class NbtUtils {
    public static CompoundTag createPosCompound(BlockPos pos) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("x", pos.getX());
        tag.putInt("y", pos.getY());
        tag.putInt("z", pos.getZ());
        return tag;
    }

    private static BlockPos blockPosFromCompound(CompoundTag tag) {
        System.out.println("getting Block pos from Compound");
        System.out.println(tag);
        return new BlockPos(tag.getInt("x"), tag.getInt("y"), tag.getInt("z"));
    }
}
