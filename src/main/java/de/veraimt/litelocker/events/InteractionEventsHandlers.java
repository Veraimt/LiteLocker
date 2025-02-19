package de.veraimt.litelocker.events;

import de.veraimt.litelocker.utils.AccessChecker;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

class InteractionEventsHandlers {

    private static InteractionResult evaluateResult(Player player, Level world, BlockPos pos) {
        if (!AccessChecker.canAccess(world, pos, player)) {
            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }

    static InteractionResult onAttackBlock(Player player, Level world, InteractionHand hand, BlockPos pos, Direction direction) {
        return evaluateResult(player, world, pos);
    }

    static void register() {
        AttackBlockCallback.EVENT.register(InteractionEventsHandlers::onAttackBlock);
    }
}
