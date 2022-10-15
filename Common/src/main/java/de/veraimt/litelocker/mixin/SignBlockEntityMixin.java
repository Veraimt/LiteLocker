package de.veraimt.litelocker.mixin;

import de.veraimt.litelocker.entities.BlockPosState;
import de.veraimt.litelocker.protection.protector.ProtectorSign;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.UUID;

@Mixin(SignBlockEntity.class)
public abstract class SignBlockEntityMixin extends BlockEntity implements ProtectorSign {

    private static final byte MAX_USERS = SignBlockEntity.LINES -1;

    private UUID[] playerUUIDs = new UUID[MAX_USERS];

    @Shadow public abstract void setMessage(int $$0, Component $$1);

    @Shadow public abstract void setEditable(boolean $$0);

    public SignBlockEntityMixin(BlockEntityType<?> $$0, BlockPos $$1, BlockState $$2) {
        super($$0, $$1, $$2);
    }


    @Inject(method = "executeClickCommands", at = @At("HEAD"))
    public void onClick(ServerPlayer player, CallbackInfoReturnable<Boolean> cir) {
        System.out.println("Sign onClick");
        if (canAccess(player)) {
            setEditable(true);
            player.openTextEdit(getBlockEntity());
        }
        //cir.setReturnValue(false);
    }

    @Override
    public void setChanged() {
        System.out.println("Method Call: "+ getClass().getName() +"#setChanged");
        System.out.println("STACKTRACE" + Arrays.toString(Thread.currentThread().getStackTrace()));
        onChanged();
        super.setChanged();
    }

    @Override
    public void setRemoved() {
        onRemoved();
        super.setRemoved();
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
        setMessage(0, new TextComponent(Tag.PRIVATE.tag));
        setChanged();
        getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
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
}
