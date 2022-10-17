![image](other/LiteLocker%20Logo.svg "LiteLocker")
A server-sided Minecraft Mod to lock Storage Containers like Chests, Barrels, Shulker Boxes, ...

# Usage
You can lock any Storage Container by placing a Sign onto it and formatting its lines like this:
- A tag `[Private]`
- User 1
- User 2
- User 3

If you have auto locking enabled (which it is by default) you can lock a Container just by right-clicking
with a Sign on it. The Sign will be automatically filled with the Private Tag and your Username

As soon as the Protection becomes active (indicated by bold formatting of the first line og the sign)
**only Users specified** on Signs that are placed onto the Container **can open and destroy it** or the 
Signs attached to it. Also, the Container **cant be emptied by Hoppers or Hopper Minecarts**.


### UUID and Name Changes
Although the name of a player won't update on the Signs if their Username was changed, they can
still access their locked Containers, because the Sign saves the UUID of the User.

#### For Developers
You can lock any block that has a BlockEntity which extends the
class `net.minecraft.world.level.block.entity.BaseContainerBlockEntity`

# Credits
This project was created with the [MultiLoader Template](https://github.com/jaredlll08/MultiLoader-Template) by [jaredlll08](https://github.com/jaredlll08).