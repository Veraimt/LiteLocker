package de.veraimt.litelocker.mixin.api;

import de.veraimt.litelocker.utils.BlockEntityExtension;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockEntity.class)
public abstract class BlockEntityMixin implements BlockEntityExtension {
}
