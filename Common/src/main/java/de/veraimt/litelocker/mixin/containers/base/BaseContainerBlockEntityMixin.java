package de.veraimt.litelocker.mixin.containers.base;

import de.veraimt.litelocker.protection.Protection;
import de.veraimt.litelocker.protection.protectable.ProtectableBlockContainer;
import de.veraimt.litelocker.utils.BlockEntityExtension;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

import javax.annotation.Nullable;

@Mixin(BaseContainerBlockEntity.class)
public abstract class BaseContainerBlockEntityMixin extends BlockEntity implements ProtectableBlockContainer, BlockEntityExtension {


    protected Protection protection;

    //Mixins

    public BaseContainerBlockEntityMixin(BlockEntityType<?> $$0, BlockPos $$1, BlockState $$2) {
        super($$0, $$1, $$2);
    }


    //Interface Overrides

    @Override
    public BaseContainerBlockEntity getBlockEntity() {
        return (BaseContainerBlockEntity) (Object) this;
    }

    @Override
    public Protection get() {
        if (protection == null)
            if (!isRemoved())
                protection = Protection.find(this);
        return protection;
    }

    @Nullable
    @Override
    public Protection getDirect() {
        return protection;
    }

    @Override
    public Protection set(Protection newVal) {
        var temp = protection;
        protection = newVal;
        return temp;
    }
}
