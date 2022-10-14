package de.veraimt.litelocker.mixin.containers;

import de.veraimt.litelocker.protection.ProtectableContainer;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HopperBlockEntity.class)
public class HopperBlockEntityMixin {

    @Inject(method = "canTakeItemFromContainer", at = @At("RETURN"), cancellable = true)
    private static void canTakeItemFromContainer(Container container, ItemStack $$1, int $$2, Direction $$3, CallbackInfoReturnable<Boolean> cir) {
        if (container instanceof ProtectableContainer c)
            cir.setReturnValue(cir.getReturnValue() && !c.hasProtector());
    }
}
