package de.veraimt.litelocker.mixin;

import de.veraimt.litelocker.LiteLocker;
import de.veraimt.litelocker.protection.protector.ProtectorItem;
import de.veraimt.litelocker.protection.protector.ProtectorSign;
import de.veraimt.litelocker.utils.SignManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;

@Mixin(SignItem.class)
public abstract class SignItemMixin extends StandingAndWallBlockItem implements ProtectorItem {

    //Used for auto-locking

    public SignItemMixin(Block $$0, Block $$1, Properties $$2) {
        super($$0, $$1, $$2);
    }

    //Inject before the Text Edit of the sign is opened
    //Used for Auto Locking
    @Inject(method = "updateCustomBlockEntityTag", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/player/Player;openTextEdit(Lnet/minecraft/world/level/block/entity/SignBlockEntity;)V",
            shift = At.Shift.BEFORE),
            cancellable = true)
    public void updateCustomBlockEntityTag(BlockPos blockPos, Level world, Player player, ItemStack itemStack, BlockState blockState,
                                           CallbackInfoReturnable<Boolean> cir) {
        System.out.println("updateCustomBlockEntityTag");
        System.out.println("STACKTRACE" + Arrays.toString(Thread.currentThread().getStackTrace()));

        if (!LiteLocker.config.getEnableAutoLocking()) {
            return;
        }

        if (player.isSecondaryUseActive())
            return;

        ProtectorSign protectorSign = (ProtectorSign) world.getBlockEntity(blockPos);
        assert protectorSign != null;

        SignManager signManager = new SignManager(protectorSign);

        if (!protectorSign.isValid()) {
            System.out.println("not valid!");
            return;
        }

        if (!signManager.tryAutoFill(player)) {
            System.out.println("no autoFill!");
            return;
        }
        signManager.activate();

        cir.cancel();
        System.out.println("cancelled");

    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {
        System.out.println("Method Call: "+ getClass().getName() +"#useOn");

        //BlockState blockState = useOnContext.getLevel().getBlockState(useOnContext.getClickedPos());
        //System.out.println("Using sign on " + blockState.getBlock());


        return super.useOn(useOnContext);
    }

    @Override
    public InteractionResult place(BlockPlaceContext $$0) {
        return super.place($$0);
    }
}
