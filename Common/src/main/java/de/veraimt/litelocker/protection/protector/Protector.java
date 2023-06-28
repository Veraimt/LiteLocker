package de.veraimt.litelocker.protection.protector;

import de.veraimt.litelocker.entities.BlockPosState;
import de.veraimt.litelocker.protection.protectable.Protectable;
import de.veraimt.litelocker.protection.protectable.ProtectableBlockContainer;
import de.veraimt.litelocker.protection.protectable.ProtectableContainer;
import de.veraimt.litelocker.utils.AccessChecker;
import de.veraimt.litelocker.utils.BlockEntityProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

import javax.annotation.Nullable;
import java.util.UUID;

import static de.veraimt.litelocker.LiteLocker.LOGGER;

public interface Protector<T extends BlockEntity> extends BlockEntityProvider<T>, Protectable {

    final class NbtKeys {
        static final String DATA = "protector";
        static final String USERS = "users";
        static final String SIZE = "size";

        private NbtKeys() {}
    }

    UUID[] getUsers();

    void setUsers(UUID[] users);

    /**
     * @return if this Protector is the main Protector for a {@link ProtectableContainer}
     */
    boolean isMain();
    /**
     * Sets this Protector as the main Protector for its attached {@link ProtectableContainer}
     */
    void setMain();

    BlockPosState getAttachedBlock();

    @Nullable
    ProtectableBlockContainer getAttachedContainer();

    default boolean isValid() {
        return isAttachedContainerValid();
    }

    default boolean isAttachedContainerValid() {
        return getAttachedContainer() != null;
    }

    default void activate() {
        var container = getAttachedContainer();

        if (container == null) {
            return;
        }

        System.out.println("container: " + container + " at " + container.getBlockEntity().getBlockPos());
        System.out.println("protectors: " + container.get().protectors());

        if (!container.hasProtector()) {
            this.setMain();
        }
        container.addProtector(this);
    }

    default void deactivate() {
        var container = getAttachedContainer();

        if (container != null) {
            container.removeProtector(this);
        }
        onDeactivate();
    }

    void onDeactivate();

    default void onRemoved() {
        deactivate();
    }

    default void onChanged() {
        var valid = isValid();
        System.out.println("onChanged, isValid: " + valid);
        if (valid)
            activate();
        else
            deactivate();
    }

    /**
     * Saves this Protectors data to the given CompoundTag
     * @param compoundTag the CompoundTag to which this Protectors data will be saved
     */
    default void saveNbt(CompoundTag compoundTag) {
        CompoundTag data = new CompoundTag();

        CompoundTag usersTag = new CompoundTag();

        var users = getUsers();

        for (int i = 0; i < users.length; i++) {
            if (users[i] != null) {
                usersTag.put(String.valueOf(i), NbtUtils.createUUID(users[i]));
            }

        }

        data.put(NbtKeys.USERS, usersTag);
        data.putInt(NbtKeys.SIZE, users.length);

        compoundTag.put(NbtKeys.DATA, data);
    }

    /**
     * Loads this Protectors data from the given CompoundTag
     * @param compoundTag the CompoundTag from which this Protectors data will be loaded
     */
    default boolean loadNbt(CompoundTag compoundTag) {
        CompoundTag data = compoundTag.getCompound(NbtKeys.DATA);

        CompoundTag userTag = data.getCompound(NbtKeys.USERS);

        if (userTag.isEmpty())
            return false;


        for (int i = 0; i < getUsers().length; i++) {
            Tag tag = userTag.get(String.valueOf(i));
            if (tag != null)
                getUsers()[i] = NbtUtils.loadUUID(tag);
        }
        return true;
    }

    default boolean hasUser(UUID playerUUID) {
        if (playerUUID == null)
            return false;
        for (var uuid : getUsers()) {
            if (playerUUID.equals(uuid))
                return true;
        }
        return false;
    }

    default void removeUser(int i) {
        getUsers()[i] = null;
    }

    @Override
    default boolean canAccess(Player player) {
        if (!isValid())
            return true;

        if (hasUser(player == null ? null : player.getUUID()))
            return true;

        //If Player is not on this Protector, search for it on the other Protectors that the attached Block has

        var blockEntity = getBlockEntity();
        var level = blockEntity.getLevel();

        if (level == null) {
            LOGGER.error("Level for Protector {} is null", blockEntity);
            LOGGER.trace(Thread.currentThread().getStackTrace());
            return false;
        }

        var attachedBlock = getAttachedBlock();
        var attachedBlockEntity = level.getBlockEntity(attachedBlock.blockPos());
        return AccessChecker.canAccess(attachedBlockEntity, player);
    }
}
