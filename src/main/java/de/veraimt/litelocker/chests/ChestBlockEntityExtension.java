package de.veraimt.litelocker.chests;

import de.veraimt.litelocker.protection.ProtectableContainer;
import de.veraimt.litelocker.protection.Protector;

import java.util.List;

public interface ChestBlockEntityExtension extends ProtectableContainer {
    List<Protector<?>> protectorsInternal();
}
