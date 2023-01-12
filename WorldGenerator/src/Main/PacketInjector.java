package Main;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_10_R1.CraftServer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.ServerListPingEvent;

import io.netty.channel.Channel;
import net.minecraft.server.v1_10_R1.MinecraftServer;
import net.minecraft.server.v1_10_R1.NetworkManager;
import net.minecraft.server.v1_10_R1.ServerConnection;

public class PacketInjector extends Thread implements Listener {

	private MinecraftServer server;
	private List<?> networkManagers;

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		this.injectOpenConnections();
	}

	@EventHandler
	public void onPing(ServerListPingEvent event) {
		this.injectOpenConnections();
	}

	public PacketInjector() {
		try {
			CraftServer craftserver = (CraftServer) Bukkit.getServer();
			Field console = craftserver.getClass().getDeclaredField("console");
			console.setAccessible(true);
			this.server = (MinecraftServer) console.get(craftserver);
			ServerConnection conn = this.server.getServerConnection();
			networkManagers = Collections.synchronizedList((List<?>) this.getNetworkManagerList(conn));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void injectOpenConnections() {
		try {
			Field field = NetworkManager.class.getDeclaredField("channel");
			field.setAccessible(true);
			for (Object manager : networkManagers) {
				Channel channel = (Channel) field.get(manager);
				if (channel.pipeline().context("82978403") == null
						&& (channel.pipeline().context("packet_handler") != null
								&& (channel.pipeline().context("packet_handler") != null))) {
					channel.pipeline().addBefore("packet_handler", "new_packet_handler", new PacketHandler());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Object getNetworkManagerList(ServerConnection conn) {
		try {
			for (Method method : conn.getClass().getDeclaredMethods()) {
				method.setAccessible(true);
				if (method.getReturnType() == List.class) {
					Object object = method.invoke(null, conn);
					return object;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}