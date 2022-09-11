package de.veraimt.litelocker.mixin.containers;

import de.veraimt.litelocker.protection.ProtectableContainer;
import de.veraimt.litelocker.protection.Protector;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin({RandomizableContainerBlockEntity.class, AbstractFurnaceBlockEntity.class})
public abstract class ContainerBlockEntityMixin extends BaseContainerBlockEntity implements ProtectableContainer {
    private Set<Protector<?>> protectors;

    //Mixins

    public ContainerBlockEntityMixin(BlockEntityType<?> $$0, BlockPos $$1, BlockState $$2) {
        super($$0, $$1, $$2);
    }



    @Override
    public void setRemoved() {
        protectors().clear();
        super.setRemoved();
    }

    @Inject(method = "stillValid", at = @At("RETURN"), cancellable = true)
    public void stillValid(Player player, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(cir.getReturnValue() && canAccess(player));
    }

    //Interface Overrides

    @Override
    public BaseContainerBlockEntity getBlockEntity() {
        return this;
    }

    @Override
    public Set<Protector<?>> protectors() {
        if (protectors == null) {
            protectors = findProtectors();
        }

        return protectors;
    }
}
