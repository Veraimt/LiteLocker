package de.veraimt.litelocker.mixin.signs;

import de.veraimt.litelocker.protection.ProtectableContainer;
import de.veraimt.litelocker.utils.AccessChecker;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SignItem.class)
public class SignItemMixin extends StandingAndWallBlockItem {
    public SignItemMixin(Block block, Block block2, Properties properties, Direction direction) {
        super(block, block2, properties, direction);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        //TODO remove debug
        System.out.println("Use On");
        var blockEntity = context.getLevel().getBlockEntity(context.getClickedPos());
        System.out.println("BlockEntity: " + blockEntity);
        if (!(blockEntity instanceof ProtectableContainer) || AccessChecker.canAccess(blockEntity, context.getPlayer())) {
            return super.useOn(context); //normal behavior, Sign placing allowed
        }
        return InteractionResult.CONSUME;
    }
}
