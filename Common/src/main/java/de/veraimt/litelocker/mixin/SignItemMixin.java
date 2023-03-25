package de.veraimt.litelocker.mixin;

import de.veraimt.litelocker.LiteLocker;
import de.veraimt.litelocker.protection.protector.ProtectorItem;
import de.veraimt.litelocker.protection.protector.ProtectorSign;
import de.veraimt.litelocker.protection.protector.autofill.SignManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SignItem.class)
public abstract class SignItemMixin extends StandingAndWallBlockItem implements ProtectorItem {
    public SignItemMixin(Block $$0, Block $$1, Properties $$2, Direction $$3) {
        super($$0, $$1, $$2, $$3);
    }

    //Used for auto-locking



    //Inject before the Text Edit of the sign is opened
    //Used for Auto Locking
    //If not marked as cancellable, throws error
    @SuppressWarnings("CancellableInjectionUsage")
    @Inject(method = "updateCustomBlockEntityTag", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/player/Player;openTextEdit(Lnet/minecraft/world/level/block/entity/SignBlockEntity;)V",
            shift = At.Shift.BEFORE),
            cancellable = true)
    public void updateCustomBlockEntityTag(BlockPos blockPos, Level world, Player player, ItemStack itemStack, BlockState blockState,
                                           CallbackInfoReturnable<Boolean> cir) {
        if (!LiteLocker.config.getEnableAutoLocking()) {
            return;
        }

        if (player.isSecondaryUseActive())
            return;

        ProtectorSign protectorSign = (ProtectorSign) world.getBlockEntity(blockPos);
        assert protectorSign != null;

        SignManager signManager = new SignManager(protectorSign);

        if (!signManager.isValid()) {
            return;
        }

        if (!signManager.tryAutoFill(player)) {
            return;
        }
        signManager.activate();

        cir.cancel();

    }
}
