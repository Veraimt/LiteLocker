package de.veraimt.litelocker.mixin.containers.base;

import de.veraimt.litelocker.protection.ProtectableContainer;
import de.veraimt.litelocker.protection.Protector;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Set;

@Mixin(BaseContainerBlockEntity.class)
public abstract class BaseContainerBlockEntityMixin extends BlockEntity implements ProtectableContainer {


    public Set<Protector<?>> protectors;

    //Mixins

    public BaseContainerBlockEntityMixin(BlockEntityType<?> $$0, BlockPos $$1, BlockState $$2) {
        super($$0, $$1, $$2);
    }


    @Override
    public void setRemoved() {
        protectors().clear();
        super.setRemoved();
        onChanged();
    }

    protected Set<Protector<?>> protectorsInternal() {
        if (protectors == null) {
            protectors = findProtectors();
        }

        return protectors;
    }

    //Interface Overrides

    @Override
    public BaseContainerBlockEntity getBlockEntity() {
        return (BaseContainerBlockEntity) (Object) this;
    }

    @Override
    public Set<Protector<?>> protectors() {
        return protectorsInternal();
    }

    @Override
    public void onChanged() {
        System.out.println("onChanged");
    }
}
