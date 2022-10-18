package de.veraimt.litelocker.protection.protector;

import de.veraimt.litelocker.entities.BlockPosState;
import de.veraimt.litelocker.protection.protectable.Protectable;
import de.veraimt.litelocker.protection.protectable.ProtectableBlockContainer;
import de.veraimt.litelocker.protection.protectable.ProtectableBlockRegistry;
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
        var attachedBlock = getAttachedBlock();
        return !ProtectableBlockRegistry.INSTANCE.isExcluded(attachedBlock.blockState().getBlock()) &&
                getAttachedContainer() != null;
    }

    default void activate() {
        var container = getAttachedContainer();

        if (container != null) {
            container.addProtector(this);

        }
    }

    default void deactivate() {
        var container = getAttachedContainer();

        if (container != null) {
            container.removeProtector(this);
        }
    }

    default void onRemoved() {
        deactivate();
    }

    default void onChanged() {
        if (isValid())
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
    default void loadNbt(CompoundTag compoundTag) {
        CompoundTag data = compoundTag.getCompound(NbtKeys.DATA);

        CompoundTag userTag = data.getCompound(NbtKeys.USERS);


        for (int i = 0; i < getUsers().length; i++) {
            Tag tag = userTag.get(String.valueOf(i));
            if (tag != null)
                getUsers()[i] = NbtUtils.loadUUID(tag);
        }
    }

    default boolean hasUser(UUID playerUUID) {
        if (playerUUID == null)
            return false;
        for (var uuid : getUsers()) {
            if (uuid == null)
                continue;
            if (uuid.equals(playerUUID))
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

        var attachedBlock = getAttachedBlock();
        var blockEntity = getBlockEntity().getLevel().getBlockEntity(attachedBlock.blockPos());
        return AccessChecker.canAccess(blockEntity, player);
    }
}
