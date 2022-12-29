package A_Main;

import java.lang.reflect.Field;
import java.util.Collections;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_10_R1.IChatBaseComponent;
import net.minecraft.server.v1_10_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_10_R1.PacketPlayOutChat;
import net.minecraft.server.v1_10_R1.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_10_R1.PacketPlayOutScoreboardTeam;
import net.minecraft.server.v1_10_R1.PacketPlayOutTitle;
import net.minecraft.server.v1_10_R1.PacketPlayOutTitle.EnumTitleAction;

public class Packets {

	public static void sendAction(Player player, String message) {
		IChatBaseComponent ic = ChatSerializer.a("{\"text\": \"" + message + "\"}");
		PacketPlayOutChat packet = new PacketPlayOutChat(ic, (byte) 2);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
	}

	public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
		PacketPlayOutTitle length = new PacketPlayOutTitle(fadeIn, stay, fadeOut);
		PacketPlayOutTitle packetTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE,
				ChatSerializer.a("{\"text\": \"" + title + "\"}"));
		PacketPlayOutTitle packetSubTitle = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE,
				ChatSerializer.a("{\"text\": \"" + subtitle + "\"}"));
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(length);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetTitle);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetSubTitle);

	}

	public static void sendTab(Player player, String header, String footer) {
		PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
		try {
			setField(packet, "a", ChatSerializer.a("{\"text\": \"" + header + "\"}"));
			setField(packet, "b", ChatSerializer.a("{\"text\": \"" + footer + "\"}"));
		} catch (Exception localException) {
		}
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
	}

	public static void sendTag(Player player, String prefix, String suffix, int integer) {
		try {
			PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam();
			
			if (integer == 0) {
				setField(packet, "a", player.getName()); //Team Name
				setField(packet, "b", player.getName()); //Display Name
				setField(packet, "c", prefix); //Prefix
				setField(packet, "d", suffix); //Suffix
				setField(packet, "h", Collections.singleton(player.getName())); //Collection Of Players
				setField(packet, "i", Integer.valueOf(0)); //Unknown integer
				
			} else if (integer == 1) {
				setField(packet, "a", player.getName());
				setField(packet, "i", Integer.valueOf(1));

			} else if (integer == 2) {
				setField(packet, "a", player.getName());
				setField(packet, "c", prefix);
				setField(packet, "d", suffix);
				setField(packet, "i", Integer.valueOf(2));
			}
			for(Player all : Bukkit.getOnlinePlayers()) {
				((CraftPlayer) all).getHandle().playerConnection.networkManager.sendPacket(packet);
			}
		} catch (Exception exc) {
		}

	}

	private static void setField(Object packet, String field, Object value) {
		try {
			Field f = packet.getClass().getDeclaredField(field);
			f.setAccessible(true);
			f.set(packet, value);
		} catch (Exception e) {
		}
	}

}