package de.veraimt.litelocker.mixin.api;

import de.veraimt.litelocker.utils.BlockEntityExtension;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(LevelChunk.class)
public abstract class LevelChunkMixin {
    @Shadow public abstract Map<BlockPos, BlockEntity> getBlockEntities();

    @Inject(method = "clearAllBlockEntities", at = @At("HEAD"))
    public void clearAllBlockEntities(CallbackInfo ci) {
        getBlockEntities().values().forEach(blockEntity -> ((BlockEntityExtension) blockEntity).onUnload());
    }
}
