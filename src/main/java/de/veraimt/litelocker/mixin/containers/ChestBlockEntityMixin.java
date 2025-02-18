package de.veraimt.litelocker.mixin.containers;

import de.veraimt.litelocker.chests.ChestBlockEntityExtension;
import de.veraimt.litelocker.mixin.containers.base.BaseContainerBlockEntityMixin;
import de.veraimt.litelocker.protection.ProtectableContainer;
import de.veraimt.litelocker.protection.Protector;
import net.minecraft.core.BlockPos;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;

@Mixin(ChestBlockEntity.class)
public class ChestBlockEntityMixin extends BaseContainerBlockEntityMixin implements ProtectableContainer, ChestBlockEntityExtension {
    public ChestBlockEntityMixin(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    @Override
    public List<Protector<?>> protectors() {
        var container = ChestBlock.getContainer(((ChestBlock) getBlockState().getBlock()), getBlockState(), getLevel(), getBlockPos(), false);
        if (container instanceof CompoundContainer) {
            return ((ProtectableContainer) container).protectors();
        }
        return protectorsInternal();
    }

    @Override
    public List<Protector<?>> protectorsInternal() {
        return super.protectors();
    }
}
