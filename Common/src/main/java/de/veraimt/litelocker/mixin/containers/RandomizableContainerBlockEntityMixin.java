package de.veraimt.litelocker.mixin.containers;

import de.veraimt.litelocker.protection.ProtectableContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;

@Mixin(RandomizableContainerBlockEntity.class)
public abstract class RandomizableContainerBlockEntityMixin extends BaseContainerBlockEntity implements ProtectableContainer {

    protected RandomizableContainerBlockEntityMixin(BlockEntityType<?> $$0, BlockPos $$1, BlockState $$2) {
        super($$0, $$1, $$2);
    }

    @Inject(method = "canOpen", at = @At("RETURN"), cancellable = true)
    public void canOpen(Player player, CallbackInfoReturnable<Boolean> cir) {
        //TODO remove Debug
        System.out.println("Method Call: "+ getClass().getName() +"#canOpen");
        System.out.println("STACKTRACE " + Arrays.toString(Thread.currentThread().getStackTrace()));

        System.out.println(protectors());



        cir.setReturnValue(cir.getReturnValue() && canAccess(player));

    }

}
