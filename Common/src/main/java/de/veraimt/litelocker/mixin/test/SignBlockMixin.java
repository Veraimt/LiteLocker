package de.veraimt.litelocker.mixin.test;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Arrays;

@Deprecated
@Mixin(SignBlock.class)
public abstract class SignBlockMixin extends BaseEntityBlock {
    protected SignBlockMixin(Properties $$0) {
        super($$0);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        System.out.println("Call to getStateForPlacement");
        return super.getStateForPlacement(context);
    }

    @Override
    public void setPlacedBy(Level world, BlockPos blockPos, BlockState blockState, @Nullable LivingEntity entity, ItemStack itemStack) {
        super.setPlacedBy(world, blockPos, blockState, entity, itemStack);

        //TODO Remove Debug
        System.out.println("Method Call: "+ getClass().getName() +"#setPlacedBy");
        System.out.println("STACKTRACE" + Arrays.toString(Thread.currentThread().getStackTrace()));
    }
}
