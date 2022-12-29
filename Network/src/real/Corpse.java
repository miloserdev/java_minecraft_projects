package real;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_10_R1.CraftServer;
import org.bukkit.craftbukkit.v1_10_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.mojang.authlib.GameProfile;

import A_Main.Main;
import World.Reflections;
import net.minecraft.server.v1_10_R1.BlockPosition;
import net.minecraft.server.v1_10_R1.Entity;
import net.minecraft.server.v1_10_R1.EntityHuman;
import net.minecraft.server.v1_10_R1.EntityPlayer;
import net.minecraft.server.v1_10_R1.MathHelper;
import net.minecraft.server.v1_10_R1.MinecraftServer;
import net.minecraft.server.v1_10_R1.PacketPlayOutBed;
import net.minecraft.server.v1_10_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_10_R1.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_10_R1.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_10_R1.PlayerInteractManager;
import net.minecraft.server.v1_10_R1.WorldServer;

public class Corpse extends Reflections {

	public static void spawn(Player p) {
		try {
			MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
			WorldServer nmsWorld = ((CraftWorld) Bukkit.getWorlds().get(0)).getHandle();
			EntityPlayer npc = new EntityPlayer(nmsServer, nmsWorld, new GameProfile(UUID.randomUUID(), p.getName()),
					new PlayerInteractManager(nmsWorld));
			Location loc = new Location(p.getWorld(), p.getLocation().getBlockX(), p.getLocation().getBlockY() + 100,
					p.getLocation().getBlockZ());
			Material last = loc.subtract(0.0, loc.getY(), 0.0).getBlock().getType();
			for (Player all : Bukkit.getOnlinePlayers()) {
				all.sendBlockChange(loc.subtract(0.0, loc.getY(), 0.0), Material.BED_BLOCK, (byte) 0);
			}
			PacketPlayOutBed bedpacket = new PacketPlayOutBed(npc,
					new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));

			PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport((Entity) npc);
			setValue((Object) packet, "b", p.getLocation().getX());
			setValue((Object) packet, "c", p.getLocation().getBlockY());
			setValue((Object) packet, "d", p.getLocation().getZ());
			setValue((Object) packet, "e", Byte.valueOf((byte) MathHelper.floor((double) (1 * 256.0f / 360.0f))));
			setValue((Object) packet, "f", Byte.valueOf((byte) MathHelper.floor((double) (1 * 256.0f / 360.0f))));
			setValue((Object) packet, "g", true);

			PacketPlayOutNamedEntitySpawn spawn = new PacketPlayOutNamedEntitySpawn((EntityHuman) npc);
			setValue((Object) spawn, "a", npc.getId());
			setValue((Object) spawn, "b", p.getUniqueId());
			setValue((Object) spawn, "c", loc.getX());
			setValue((Object) spawn, "d", loc.getY());
			setValue((Object) spawn, "e", loc.getZ());

			for (Player all : Bukkit.getOnlinePlayers()) {
				((CraftPlayer) all).getHandle().playerConnection.sendPacket(spawn);
				((CraftPlayer) all).getHandle().playerConnection.sendPacket(packet);
				((CraftPlayer) all).getHandle().playerConnection.sendPacket(bedpacket);
			}

			new BukkitRunnable() {
				@Override
				public void run() {
						for (Player all : Bukkit.getOnlinePlayers()) {
							PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(npc.getId());
							((CraftPlayer) all).getHandle().playerConnection.sendPacket(destroy);
							all.sendBlockChange(loc.subtract(0.0, loc.getY(), 0.0), last, (byte) 0);
						}
						cancel();
				}
			}.runTaskLater(Main.getPlugin(), 100);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}