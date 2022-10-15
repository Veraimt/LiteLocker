package de.veraimt.litelocker.mixin.containers.base;

import de.veraimt.litelocker.protection.protectable.ProtectableContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({RandomizableContainerBlockEntity.class, AbstractFurnaceBlockEntity.class})
public abstract class ContainerBlockEntityMixin extends BaseContainerBlockEntityMixin implements ProtectableContainer {

    //Mixins

    public ContainerBlockEntityMixin(BlockEntityType<?> $$0, BlockPos $$1, BlockState $$2) {
        super($$0, $$1, $$2);
    }

    @Inject(method = "stillValid", at = @At("RETURN"), cancellable = true)
    public void stillValid(Player player, CallbackInfoReturnable<Boolean> cir) {
        //Minecraft Info
        //This Method is called every Tick from the Player as long as he has this container open
        cir.setReturnValue(cir.getReturnValue() && canAccess(player));
    }
}
