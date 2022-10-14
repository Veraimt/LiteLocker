package de.veraimt.litelocker.mixin.blocks;

import de.veraimt.litelocker.protection.ProtectableContainer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.BlastFurnaceBlock;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.block.SmokerBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.OptionalInt;

@Mixin({FurnaceBlock.class, BlastFurnaceBlock.class, SmokerBlock.class})
public abstract class FurnaceBlockMixin extends AbstractFurnaceBlock {
    protected FurnaceBlockMixin(Properties $$0) {
        super($$0);
    }

    @Redirect(method = "openContainer", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/player/Player;openMenu(Lnet/minecraft/world/MenuProvider;)Ljava/util/OptionalInt;"))
    public OptionalInt openMenu(Player player, MenuProvider menuProvider) {
        if (!(menuProvider instanceof ProtectableContainer c)) {
            //not protectable
            return player.openMenu(menuProvider);
        }
        if (c.canAccess(player)) {
            return player.openMenu(menuProvider);
        } else {
            return player.openMenu(null);
        }
    }
}
