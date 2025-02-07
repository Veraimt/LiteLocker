package de.veraimt.litelocker.mixin.signs;

import de.veraimt.litelocker.protection.ProtectableContainer;
import de.veraimt.litelocker.signs.ProtectorSign;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.SignText;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(SignBlockEntity.class)
public abstract class SignBLockEntityMixin extends BlockEntity implements ProtectorSign {

    @Shadow private SignText frontText;

    public SignBLockEntityMixin(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    @Unique
    private final UUID[] users = new UUID[SignText.LINES - 1];

    //TODO Implement
    @Override
    public UUID[] getUsers() {
        return users;
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


    @Inject(method = "updateSignText",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;sendBlockUpdated(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockState;I)V",
                    shift = At.Shift.BEFORE
            )
    )
    public void updateSignText(CallbackInfo ci) {
        activate();
    }

    @Inject(method = "markUpdated", at = @At("HEAD"))
    public void markUpdated(CallbackInfo ci) {
        //TODO remove debug
        System.out.println("markUpdated");
        //be cautious when calling methods here as this might result in recursive markUpdated calls
    }

    @Inject(method = "saveAdditional", at = @At("TAIL"))
    public void saveAdditional(CompoundTag compoundTag, HolderLookup.Provider provider, CallbackInfo ci) {
        saveNbt(compoundTag);
    }

    @Inject(method = "loadAdditional", at = @At("TAIL"))
    public void loadAdditional(CompoundTag compoundTag, HolderLookup.Provider provider, CallbackInfo ci) {
        if(!loadNbt(compoundTag))
            return;
        updateGameProfilesOnLoad(signText -> {
            frontText = signText;
        }); //silently update the sign text
    }
}
