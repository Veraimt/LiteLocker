package de.veraimt.litelocker.mixin.containers;

import de.veraimt.litelocker.mixin.containers.base.RandomizableContainerBlockEntityMixin;
import de.veraimt.litelocker.protection.ProtectableBlockContainer;
import de.veraimt.litelocker.protection.ProtectableContainer;
import de.veraimt.litelocker.protection.Protector;
import net.minecraft.core.BlockPos;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

import java.util.UUID;
import java.util.function.Consumer;

@Mixin(ChestBlockEntity.class)
public class ChestBlockEntityMixin extends RandomizableContainerBlockEntityMixin {

    protected ChestBlockEntityMixin(BlockEntityType<?> $$0, BlockPos $$1, BlockState $$2) {
        super($$0, $$1, $$2);
    }

    private Container getContainer() {
        return ChestBlock.getContainer(((ChestBlock) getBlockState().getBlock()), getBlockState(), getLevel(), getBlockPos(), false);
    }

    //Method overrides


    @Override
    public void addProtector(Protector<?> protector) {
        super.addProtector(protector);
    }

    @Override
    public void removeProtector(Protector<?> protector) {
        if (getContainer() instanceof CompoundContainer c)
            ((ProtectableContainer) c).removeProtector(protector);
        super.removeProtector(protector);
    }

    @Override
    public boolean hasProtector() {
        if (getContainer() instanceof CompoundContainer c)
            return ((ProtectableContainer) c).hasProtector();
        return super.hasProtector();
    }
    @Override
    public boolean hasProtector(Protector<?> protector) {
        if (getContainer() instanceof CompoundContainer c)
            return ((ProtectableContainer) c).hasProtector(protector);
        return super.hasProtector(protector);
    }

    @Override
    public boolean hasUser(UUID playerUUID) {
        if (getContainer() instanceof CompoundContainer c)
            return ((ProtectableContainer) c).hasUser(playerUUID);
        return super.hasUser(playerUUID);
    }
}
