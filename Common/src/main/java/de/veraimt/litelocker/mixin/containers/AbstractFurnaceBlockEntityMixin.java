package de.veraimt.litelocker.mixin.containers;

import de.veraimt.litelocker.protection.ProtectableContainer;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class AbstractFurnaceBlockEntityMixin implements ProtectableContainer {

}
