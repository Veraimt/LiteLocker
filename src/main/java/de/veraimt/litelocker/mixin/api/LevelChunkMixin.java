package de.veraimt.litelocker.mixin.api;

import de.veraimt.litelocker.utils.BlockEntityExtension;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

import java.util.Collection;
import java.util.function.Consumer;

@Mixin(LevelChunk.class)
public abstract class LevelChunkMixin {

    /** Injects into the {@link Collection#forEach(Consumer)} instruction in {@link LevelChunk#clearAllBlockEntities()}
     * which calls {@link BlockEntity#setRemoved()} for every BlockEntity in the Chunk
     */
    @Redirect(method = "clearAllBlockEntities", at = @At(value = "INVOKE", target = "Ljava/util/Collection;forEach(Ljava/util/function/Consumer;)V"),
            slice = @Slice(
                    from = @At(value = "HEAD"),
                    to = @At(value = "INVOKE", target = "Ljava/util/Map;clear()V")
            ))
    public <T> void setBlockEntitiesRemoved(Collection<T> blockEntities, Consumer<T> consumer) {
        Consumer<T> instruction = blockEntity -> {
            //Unload
            ((BlockEntityExtension) blockEntity).onUnload();

            //Original Instruction
            consumer.accept(blockEntity);
        };
        //For each
        blockEntities.forEach(instruction);
    }
}
