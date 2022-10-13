package de.veraimt.litelocker.protection;

import de.veraimt.litelocker.entities.BlockPosState;
import de.veraimt.litelocker.utils.BlockEntityProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import javax.annotation.Nullable;
import java.util.UUID;

public interface Protector<T extends BlockEntity> extends BlockEntityProvider<T> {

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
    default ProtectableBlockContainer getAttachedContainer() {
        var attachedBlockEntity = getBlockEntity().getLevel().getBlockEntity(getAttachedBlock().blockPos());
        return attachedBlockEntity instanceof ProtectableBlockContainer ? ((ProtectableBlockContainer) attachedBlockEntity) : null;
    }

    default boolean isValid() {
        var attachedBlock = getAttachedBlock();
        return !ProtectableBlockRegistry.INSTANCE.isExcluded(attachedBlock.blockState().getBlock()) &&
                getAttachedContainer() != null;
    }

    default void activate() {
        System.out.println("Activating protection for " + getAttachedContainer());


        var container = getAttachedContainer();

        if (container != null) {
            container.addProtector(this);

        }
    }

    default void deactivate() {
        var container = getAttachedContainer();

        if (container != null)
            container.removeProtector(this);
    }

    default void onRemoved() {
        deactivate();
    }

    /**
     * Saves this Protectors data to the given CompoundTag
     * @param compoundTag the CompoundTag to which this Protectors data will be saved
     */
    default void saveNbt(CompoundTag compoundTag) {
        System.out.println("Save Nbt");
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
        System.out.println("Load Nbt");
        CompoundTag data = compoundTag.getCompound(NbtKeys.DATA);

        CompoundTag userTag = data.getCompound(NbtKeys.USERS);


        for (int i = 0; i < getUsers().length; i++) {
            Tag tag = userTag.get(String.valueOf(i));
            if (tag != null)
                getUsers()[i] = NbtUtils.loadUUID(tag);
        }
    }

    default boolean hasUser(UUID playerUUID) {
        for (var uuid : getUsers()) {
            if (playerUUID.equals(uuid))
                return true;
        }
        return false;
    }

    default void removeUser(int i) {
        Level world = getBlockEntity().getLevel();
        ProtectableContainer container = getAttachedContainer();
        if (container != null) {
            Player player = world.getServer().getPlayerList().getPlayer(getUsers()[i]);
            if (player != null) {
                System.out.println("!!removed Player: " + player.getName() + " from Protector: " + this);
            }
        }
        getUsers()[i] = null;
    }
}
