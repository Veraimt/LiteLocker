package de.veraimt.litelocker.mixin;

import de.veraimt.litelocker.entities.BlockPosState;
import de.veraimt.litelocker.protection.protectable.ProtectableBlockContainer;
import de.veraimt.litelocker.protection.protector.ProtectorSign;
import de.veraimt.litelocker.utils.BlockEntityExtension;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(SignBlockEntity.class)
public abstract class SignBlockEntityMixin extends BlockEntity implements ProtectorSign, BlockEntityExtension {

    private static final byte MAX_USERS = SignBlockEntity.LINES -1;
    private ProtectableBlockContainer attachedContainer;
    private boolean unloaded = false;

    private UUID[] playerUUIDs = new UUID[MAX_USERS];

    @Shadow public abstract void setMessage(int $$0, Component $$1);

    @Shadow public abstract void setEditable(boolean $$0);

    public SignBlockEntityMixin(BlockEntityType<?> $$0, BlockPos $$1, BlockState $$2) {
        super($$0, $$1, $$2);
    }


    @Inject(method = "executeClickCommands", at = @At("HEAD"))
    public void onClick(ServerPlayer player, CallbackInfoReturnable<Boolean> cir) {
        if (canAccess(player) && isValid()) {
            setEditable(true);
            player.openTextEdit(getBlockEntity());
        }
        //cir.setReturnValue(false);
    }

    @Override
    public void setChanged() {
        onChanged();
        super.setChanged();
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        if (unloaded)
            return;
        onRemoved();
        attachedContainer = null;
    }

    @Inject(method = "saveAdditional", at = @At("HEAD"))
    public void saveAdditional(CompoundTag compoundTag, CallbackInfo ci) {
        saveNbt(compoundTag);
    }

    @Inject(method = "load", at = @At("TAIL"))
    public void load(CompoundTag compoundTag, CallbackInfo ci) {
        loadNbt(compoundTag);
    }

    //Interface Overrides

    @Override
    public void onUnload() {
        unloaded = true;
    }

    @Override
    public SignBlockEntity getBlockEntity() {
        return (SignBlockEntity) (Object) this;
    }

    @Override
    public UUID[] getUsers() {
        return playerUUIDs;
    }

    @Override
    public void setUsers(UUID[] users) {
        if (users.length > MAX_USERS) {
            throw new IllegalArgumentException("Array of Users is too long (" + users.length + "), max=" + MAX_USERS);
        }
        this.playerUUIDs = users;
    }

    @Override
    public void setMain() {
        if (isRemoved())
            return;
        setMessage(0, Component.nullToEmpty(Tag.PRIVATE.tag));
        setChanged();
        updateWorld();
    }

    @Override
    public BlockPosState getAttachedBlock() {
        Direction facing;
        if (getBlockState().hasProperty(WallSignBlock.FACING)) {
            facing = getBlockState().getValue(WallSignBlock.FACING);
        } else {
            facing = Direction.UP;
        }
        BlockPos pos = getBlockPos();
        BlockPos targetPos = pos.relative(facing, -1);

        return new BlockPosState(targetPos, getLevel().getBlockState(targetPos));
    }

    @Nullable
    @Override
    public ProtectableBlockContainer getAttachedContainer() {
        if (attachedContainer == null)
            attachedContainer = getAttachedContainerInternal();
        return attachedContainer;
    }

    private ProtectableBlockContainer getAttachedContainerInternal() {
        var attachedBlockEntity = getBlockEntity().getLevel().getBlockEntity(getAttachedBlock().blockPos());
        return attachedBlockEntity instanceof ProtectableBlockContainer ? ((ProtectableBlockContainer) attachedBlockEntity) : null;
    }
}
