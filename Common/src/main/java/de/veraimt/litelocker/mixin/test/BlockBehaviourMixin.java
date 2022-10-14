package de.veraimt.litelocker.mixin.test;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;

@Deprecated
@Mixin({BlockBehaviour.class})
public abstract class BlockBehaviourMixin {


    @Inject(method = "use", at = @At("RETURN"), cancellable = true)
    public void use(BlockState blockState, Level world, BlockPos blockPos, Player player, InteractionHand hand,
                    BlockHitResult blockHitResult, CallbackInfoReturnable<InteractionResult> cir) {
        System.out.println("Method Call: "+ getClass().getName() +"#use");
        System.out.println("STACKTRACE" + Arrays.toString(Thread.currentThread().getStackTrace()));

        //System.out.println("Namespace: "+Registry.BLOCK.getKey((Block) (Object)this).getNamespace());
        //System.out.println("Path: "+Registry.BLOCK.getKey((Block) (Object)this).getPath());
        System.out.println("ResourceLocation: " + Registry.BLOCK.getKey((Block) (Object)this));
        //System.out.println(Registry.BLOCK.get(ResourceLocation.tryParse("minecraft:grass_block")));
        //System.out.println("Grass Block? :" + Registry.BLOCK.get(ResourceLocation.tryParse("minecraft:grass_block")).equals(this));

    }

    @Inject(method = "use", at = @At("RETURN"))
    public void useReturn(BlockState blockState, Level world, BlockPos blockPos, Player player, InteractionHand hand,
                    BlockHitResult blockHitResult, CallbackInfoReturnable<InteractionResult> cir) {

        System.out.println(cir.getReturnValue());
    }

}
