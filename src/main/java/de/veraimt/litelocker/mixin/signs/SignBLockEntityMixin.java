package de.veraimt.litelocker.mixin.signs;

import de.veraimt.litelocker.protection.ProtectableContainer;
import de.veraimt.litelocker.protection.Protector;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

import java.util.UUID;

@Mixin(SignBlockEntity.class)
public abstract class SignBLockEntityMixin extends BlockEntity implements Protector<SignBlockEntity> {

    public SignBLockEntityMixin(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    //TODO Implement
    @Override
    public UUID[] getUsers() {
        return new UUID[0];
    }

    @Override
    public void setUsers(UUID[] users) {

    }

    @Override
    public boolean isMain() {
        return false;
    }

    @Override
    public void setMain() {

    }

    @Override
    public @Nullable ProtectableContainer getAttachedContainer() {
        if (!getBlockState().hasProperty(WallSignBlock.FACING))
            return null;
        var facing = getBlockState().getValue(WallSignBlock.FACING);
        var pos = getBlockPos().relative(facing, -1);

        var blockEntity = getLevel().getBlockEntity(pos);
        if (blockEntity instanceof ProtectableContainer protectableContainer) {
            return protectableContainer;
        }
        return null;
    }

    @Override
    public SignBlockEntity getBlockEntity() {
        return (SignBlockEntity) (Object) this;
    }
}
