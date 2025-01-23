package de.veraimt.litelocker.mixin.containers.base;

import de.veraimt.litelocker.protection.ProtectableContainer;
import de.veraimt.litelocker.protection.Protector;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.LockCode;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ArrayList;
import java.util.List;

@Mixin(BaseContainerBlockEntity.class)
public class BaseContainerBlockEntityMixin extends BlockEntity implements ProtectableContainer {

    public BaseContainerBlockEntityMixin(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    private final BlockPos[] directions = new BlockPos[]{
            getBlockPos().north(),
            getBlockPos().east(),
            getBlockPos().south(),
            getBlockPos().west()
    };

    @Override
    public List<Protector<?>> protectors() {
        if (getLevel() == null) { //error
            return null;
        }

        var list = new ArrayList<Protector<?>>(directions.length);
        for (BlockPos pos : directions) {
            var blockEntity = getLevel().getBlockEntity(pos);
            if (blockEntity instanceof Protector<?>) {
                list.add((Protector<?>) blockEntity);
            }
        }

        return list;
    }

    @Override
    public BaseContainerBlockEntity getBlockEntity() {
        return (BaseContainerBlockEntity) (Object) this;
    }


    /* Injects and Redirects */

    @Unique
    private static final LockCode CLOSED_LOCK = new LockCode("\0");
    @Redirect(method = "canOpen", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/BaseContainerBlockEntity;canUnlock(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/LockCode;Lnet/minecraft/network/chat/Component;)Z"))
    public boolean canOpen(Player player, LockCode lockCode, Component component) {
        if (!canAccess(player))
            return BaseContainerBlockEntity.canUnlock(player, CLOSED_LOCK, component); //emulate container being locked
        return BaseContainerBlockEntity.canUnlock(player, lockCode, component); //try to open container normally
    }
}
