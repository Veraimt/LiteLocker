![image](other/LiteLocker%20Logo.svg "LiteLocker")
A server-sided Minecraft Mod to lock Storage Containers like Chests, Barrels, Furnaces, ...

# Usage
You can lock any Storage Container by placing a Sign onto it and formatting its lines like this:
- Tag `[Private]` or `[More Users]`
- User 1 e.g. `Notch`
- User 2
- User 3

If you have auto locking enabled (which it is by default) you can lock a Container just by right-clicking
with a Sign on it. The Sign will be automatically filled with the Private Tag and your Username.

As soon as the Protection becomes active (indicated by bold formatting of the first line of the sign)
**only Users specified** on Signs that are placed onto the Container **can open and destroy it** or the
Signs attached to it. Also, the Container **cant be emptied by Hoppers or Hopper Minecarts**.

The only other way to destroy a protected Block is using the `/setblock` Command. <br>
_(In the future there will be configurable ways for Server-Operators to check a Containers contents or
remove a protected Block)_

If you want more Players to have access than you can fit on one Sign, just add another Sign onto
the Container. In the first line of the sign you have to enter the Tag `[More Users]`. In the other
lines you can specify the Players you want to grant access to.

## UUID and Name Changes
Although the name of a Player won't update on the Signs if their Username was changed, they can
still access their locked Containers, because the Sign saves the UUID of the User
in its NBT Data.

## Known Bugs
The following "bugs" are less important edge cases which are not yet implemented

- If you place a Container below a locked Hopper which is pointing downwards, it will push its Items
into the Container below.

- Although locked Shulker Boxes can't be broken by a Player they can still be broken by using a Piston.

Following bug is caused by Minecraft itself
- If you break a protected Sign, it loses its description; this is client-side only



## For Developers
You can lock any block that has a BlockEntity which extends the
class `net.minecraft.world.level.block.entity.BaseContainerBlockEntity`

### Mod Compatibility
I try to make LiteLocker compatible with as many Mods as possible, but this is not prioritized.
If you spot any incompatibilities with other Mods, report them under Issues.

# Credits
This project was created with the [MultiLoader Template](https://github.com/jaredlll08/MultiLoader-Template) by [jaredlll08](https://github.com/jaredlll08).
