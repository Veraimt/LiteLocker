package de.veraimt.litelocker.mixin.containers;

import de.veraimt.litelocker.chests.ChestBlockEntityExtension;
import de.veraimt.litelocker.protection.ProtectableContainer;
import de.veraimt.litelocker.protection.Protector;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.List;

@Mixin(CompoundContainer.class)
public class CompoundContainerMixin implements ProtectableContainer {
    @Shadow @Final private Container container1;
    @Shadow @Final private Container container2;


    private ChestBlockEntityExtension container1() {
        return (ChestBlockEntityExtension) container1;
    }
    private ChestBlockEntityExtension container2() {
        return (ChestBlockEntityExtension) container2;
    }

    @Override
    public List<Protector<?>> protectors() {
        var protectors = new ArrayList<>(container1().protectorsInternal());
        protectors.addAll(container2().protectorsInternal());
        return protectors;
    }
}
