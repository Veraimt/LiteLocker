package de.veraimt.litelocker.mixin.containers.base;

import de.veraimt.litelocker.protection.ProtectableBlockContainer;
import de.veraimt.litelocker.protection.ProtectableContainer;
import de.veraimt.litelocker.protection.Protection;
import de.veraimt.litelocker.protection.Protector;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.UUID;

@Mixin(CompoundContainer.class)
public class CompoundContainerMixin implements ProtectableContainer {

    @Shadow @Final private Container container1;
    @Shadow @Final private Container container2;


    private ProtectableBlockContainer container1() {
        return ((ProtectableBlockContainer) container1);
    }
    private ProtectableBlockContainer container2() {
        return ((ProtectableBlockContainer) container2);
    }

    @Override
    public void addProtector(Protector<?> protector) {
        var attachedContainer = protector.getAttachedContainer();
        if (container1().equals(attachedContainer)) {
            container1().get().addProtector(protector);
        } else if (container2().equals(attachedContainer)) {
            container2().get().addProtector(protector);
        }
    }

    @Override
    public void removeProtector(Protector<?> protector) {
        if (!container1().get().removeProtector(protector))
            container2().get().removeProtector(protector);
    }

    @Override
    public boolean hasProtector() {
        return container1().get().hasProtector() || container2().get().hasProtector();
    }

    @Override
    public boolean hasProtector(Protector<?> protector) {
        return container1().get().hasProtector(protector) || container2().get().hasProtector(protector);
    }

    @Override
    public boolean hasUser(UUID playerUUID) {
        return container1().get().hasUser(playerUUID) || container2().get().hasUser(playerUUID);
    }
}
