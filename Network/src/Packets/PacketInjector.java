package Packets;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_10_R1.CraftServer;
import org.bukkit.event.Listener;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.minecraft.server.v1_10_R1.MinecraftServer;
import net.minecraft.server.v1_10_R1.NetworkManager;
import net.minecraft.server.v1_10_R1.ServerConnection;

public class PacketInjector implements Listener {
	
	//public static List<String> mylistStr = new ArrayList<>();
	

	public static void setListener(Channel channel, String str, ChannelHandler cl) throws Exception {
		if (channel.pipeline().context(str) == null && (channel.pipeline().context("packet_handler") != null)) {
			channel.pipeline().addBefore("packet_handler", str, cl);
		}
	}

	static {
		a();
	}

	public static Thread thread;

	public static void a() {
		thread = new Thread() {

			public void run() {
				Channel channel = null;
				try {
					while (true) {
						CraftServer craftserver = (CraftServer) Bukkit.getServer();
						Field console = craftserver.getClass().getDeclaredField("console");
						console.setAccessible(true);
						MinecraftServer server = (MinecraftServer) console.get(craftserver);
						Field field = NetworkManager.class.getDeclaredField("channel");
						field.setAccessible(true);
						ServerConnection conn = server.getServerConnection();
						for (Method method : conn.getClass().getDeclaredMethods()) {
							method.setAccessible(true);
							if (method.getReturnType() == List.class) {
								Object object = method.invoke(null, conn);
								for (Object manager : Collections.synchronizedList((List<?>) object)) {
									channel = (Channel) field.get(manager);
								}
							}
						}
						if (channel != null && channel.pipeline().context("packet_handler") != null
								&& channel.pipeline().context("new_packet_handler") == null
								&& (channel.pipeline().context("packet_handler") != null)) {

							channel.pipeline().addBefore("packet_handler", "new_packet_handler",
									new ChannelDuplexHandler() {
										@Override
										public void channelRead(ChannelHandlerContext context, Object packet)
												throws Exception {
											PacketEvent event = new PacketEvent(packet, context.channel());
											Bukkit.getPluginManager().callEvent(event);
											System.out.println("\033[Read " + packet.getClass().getSimpleName());
											super.channelRead(context, event.getPacket());
										}

										@Override
										public void write(ChannelHandlerContext context, Object packet, ChannelPromise promise) throws Exception {
											PacketEvent event = new PacketEvent(packet, context.channel());
											Bukkit.getPluginManager().callEvent(event);
											System.out.println("\033[Write " + packet.getClass().getSimpleName());
											super.write(context, event.getPacket(), promise);
										};
									});
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		thread.start();
	}

}
