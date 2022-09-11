package de.veraimt.litelocker.mixin;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import de.veraimt.litelocker.LiteLocker;
import de.veraimt.litelocker.protection.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockState.class)
public abstract class BlockStateMixin extends BlockBehaviour.BlockStateBase {

    protected BlockStateMixin(Block $$0, ImmutableMap<Property<?>, Comparable<?>> $$1, MapCodec<BlockState> $$2) {
        super($$0, $$1, $$2);
    }

    @Override
    public void onRemove(Level world, BlockPos blockPos, BlockState blockState, boolean b) {
        //System.out.println("Method Call: "+ getClass().getName() +"#onRemove");
        //System.out.println("STACKTRACE " + Arrays.toString(Thread.currentThread().getStackTrace()));
        //TODO ??
        super.onRemove(world, blockPos, blockState, b);
    }

    @Override
    public InteractionResult use(Level world, Player player, InteractionHand hand, BlockHitResult blockHitResult) {

        System.out.println("Method Call: "+ getClass().getName() +"#use");

        System.out.println("using " + getBlock());

        //TODO REMOVE
        if (true)
            return super.use(world, player, hand, blockHitResult);

        if (!LiteLocker.config.getEnableAutoLocking()) {
            return super.use(world, player, hand, blockHitResult);
        }

        if (!hasBlockEntity())
            return super.use(world, player, hand, blockHitResult);

        BlockEntity blockEntity = world.getBlockEntity(blockHitResult.getBlockPos());


        if (blockEntity instanceof ProtectableContainer container) {
            System.out.println("Protectable Container");
            if (player.getItemInHand(hand).getItem() instanceof SignItem) {
                //Attempt to protect block with sign
                return InteractionResult.PASS;

                //Next the ItemStack will be used
                //See ItemStackMixin
            }

            System.out.println(container.protectors());

            if (container.hasProtector()) {
                if (container.protectors().stream().noneMatch(protector -> protector.hasUser(player.getUUID()))) {
                    return InteractionResult.SUCCESS;
                }
            }

            //allow normal access if player is permitted or there is no protection
            return super.use(world, player, hand, blockHitResult);
        }



        //Block cant be protected, normal access

        return super.use(world, player, hand, blockHitResult);

    }
}