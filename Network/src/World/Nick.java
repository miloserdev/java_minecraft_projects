package World;

import java.lang.reflect.Field;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.mojang.authlib.GameProfile;

import A_Main.Main;
import net.minecraft.server.v1_10_R1.Packet;
import net.minecraft.server.v1_10_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_10_R1.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_10_R1.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_10_R1.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_10_R1.PacketPlayOutSpawnEntityLiving;

public class Nick {
	public static HashMap<Player, String> nickers = new HashMap<>();

	public static void a(Player p, String name) {
		if (!nickers.containsKey(p))
			nickers.put(p, p.getName());
		CraftPlayer cp = (CraftPlayer) p;
		try {
			getField(GameProfile.class, "name").set(cp.getProfile(), name);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		PacketPlayOutSpawnEntityLiving destroy = new PacketPlayOutSpawnEntityLiving();
		sendPacket(destroy);
		removeFromTab(cp);
		new BukkitRunnable() {
			@Override
			public void run() {
				addToTab(cp);
				PacketPlayOutNamedEntitySpawn spawn = new PacketPlayOutNamedEntitySpawn(cp.getHandle());
				for (Player all : Bukkit.getOnlinePlayers()) {
					if (!all.equals(p)) {
						((CraftPlayer) all).getHandle().playerConnection.sendPacket(spawn);
					}
				}
			}
		}.runTaskLater(Main.getPlugin(), 4);
	}

	public static void nick(Player p, String name) {
		if (!nickers.containsKey(p))
			nickers.put(p, p.getName());
		CraftPlayer cp = (CraftPlayer) p;
		try {
			getField(GameProfile.class, "name").set(cp.getProfile(), name);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(cp.getEntityId());
		sendPacket(destroy);
		removeFromTab(cp);
		new BukkitRunnable() {
			@Override
			public void run() {
				addToTab(cp);
				PacketPlayOutNamedEntitySpawn spawn = new PacketPlayOutNamedEntitySpawn(cp.getHandle());
				for (Player all : Bukkit.getOnlinePlayers()) {
					if (!all.equals(p)) {
						((CraftPlayer) all).getHandle().playerConnection.sendPacket(spawn);
					}
				}
			}
		}.runTaskLater(Main.getPlugin(), 4);
	}

	public static void unnick(Player p) {
		String name = nickers.get(p);
		if (nickers.containsKey(p))
			nickers.remove(p);
		CraftPlayer cp = (CraftPlayer) p;
		try {
			getField(GameProfile.class, "name").set(cp.getProfile(), name);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(cp.getEntityId());
		sendPacket(destroy);
		removeFromTab(cp);
		new BukkitRunnable() {
			@Override
			public void run() {
				addToTab(cp);
				PacketPlayOutNamedEntitySpawn spawn = new PacketPlayOutNamedEntitySpawn(cp.getHandle());
				for (Player all : Bukkit.getOnlinePlayers()) {
					if (!all.equals(p)) {
						((CraftPlayer) all).getHandle().playerConnection.sendPacket(spawn);
					}
				}
			}
		}.runTaskLater(Main.getPlugin(), 4);
	}

	private static void removeFromTab(CraftPlayer p) {
		PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, p.getHandle());
		sendPacket(packet);
	}

	private static void addToTab(CraftPlayer p) {
		PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, p.getHandle());
		sendPacket(packet);
	}

	private static Field getField(Class<?> clazz, String name) {
		try {
			Field f = clazz.getDeclaredField(name);
			f.setAccessible(true);
			return f;
		} catch (NoSuchFieldException | SecurityException e) {
			return null;
		}
	}

	@SuppressWarnings("rawtypes")
	private static void sendPacket(Packet packet) {
		for (Player all : Bukkit.getOnlinePlayers()) {
			((CraftPlayer) all).getHandle().playerConnection.sendPacket(packet);
		}
	}
}