package znn;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_10_R1.CraftServer;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;

import com.mojang.authlib.GameProfile;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import net.minecraft.server.v1_10_R1.MinecraftServer;
import net.minecraft.server.v1_10_R1.NetworkManager;
import net.minecraft.server.v1_10_R1.PacketLoginInStart;
import net.minecraft.server.v1_10_R1.ServerConnection;

public class NettyInjection {

	private String handlerName = "nettyinjection_2";
	private Map<String, PacketHandler> handlerList = new HashMap<String, PacketHandler>();
	private Listener listener;

	private final HashMap<String, Channel> playerChannel = new HashMap<String, Channel>();

	private final List<Channel> globalChannel = new ArrayList<Channel>();
	private ChannelInboundHandlerAdapter globalHandler;

	public interface PacketHandler {

		public default Object onPacketIn(Player sender, Channel channel, Object packet) {
			return packet;
		}

		public default Object onPacketOut(Player target, Channel channel, Object packet) {
			return packet;
		}

	}

	public NettyInjection(Plugin plugin, String handlerName) {
		this.handlerName = "inject_" + handlerName;

		Bukkit.getPluginManager().registerEvents(listener = new Listener() {
			@EventHandler
			public final void onPlayerLogin(PlayerLoginEvent event) {
				inject(event.getPlayer());
			}

			@EventHandler
			public void onDisabled(PluginDisableEvent event) {
				if (event.getPlugin().equals(plugin)) {
					disable();
				}
			}
		}, plugin);

		ChannelInitializer<Channel> last = new ChannelInitializer<Channel>() {
			@Override
			protected void initChannel(Channel channel) throws Exception {
				injectChannel(channel);
			}
		};

		ChannelInitializer<Channel> first = new ChannelInitializer<Channel>() {
			@Override
			protected void initChannel(Channel channel) throws Exception {
				channel.pipeline().addLast(last);
			}
		};

		globalHandler = new ChannelInboundHandlerAdapter() {
			@Override
			public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
				((Channel) msg).pipeline().addFirst(first);
				super.channelRead(ctx, msg);
			}
		};

		registerGlobalChannel();

		for (Player player : Bukkit.getOnlinePlayers()) {
			inject(player);
		}
	}

	@SuppressWarnings("unchecked")
	private final void registerGlobalChannel() {
		MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
		ServerConnection connection = server.am();

		List<Object> channelFuture = (List<Object>) get(connection, "g");
		for (Object item : channelFuture) {
			if (!ChannelFuture.class.isInstance(item))
				break;
			Channel channel = ((ChannelFuture) item).channel();
			globalChannel.add(channel);
			channel.pipeline().addFirst(globalHandler);
		}
	}

	private final void unregisterGlobalChannel() {
		for (Channel global : globalChannel) {
			final ChannelPipeline pipe = global.pipeline();
			global.eventLoop().execute(new Runnable() {
				@Override
				public void run() {
					try {
						pipe.remove(globalHandler);
					} catch (Exception e) {
					}
				}
			});
		}
	}

	public final void addHandler(String name, PacketHandler handler) {
		handlerList.put(name, handler);
	}

	public final void removeHandler(String name) {
		if (handlerList.containsKey(name))
			handlerList.remove(name);
	}

	public final void inject(Player player) {
		injectChannel(getChannel(player)).player = player;
	}

	public final void uninject(Player player) {
		uninjectChannel(getChannel(player));
	}

	private final Channel getChannel(Player player) {
		Channel channel = playerChannel.get(player.getName());
		if (channel == null) {
			try {
				NetworkManager manager = ((CraftPlayer) player).getHandle().playerConnection.networkManager;
				channel = (Channel) get(manager, "channel");
				playerChannel.put(player.getName(), channel);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return channel;
	}

	private final PacketInjection injectChannel(Channel channel) {
		try {
			PacketInjection handel = (PacketInjection) channel.pipeline().get(handlerName);
			if (handel == null) {
				handel = new PacketInjection();
				channel.pipeline().addBefore("packet_handler", handlerName, handel);
			}
			return handel;
		} catch (Exception e) {
			return (PacketInjection) channel.pipeline().get(handlerName);
		}
	}

	private final void uninjectChannel(Channel channel) {
		Object handel = channel.pipeline().get(handlerName);
		if (handel != null) {
			channel.pipeline().remove(handlerName);
		}
	}

	public final void disable() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			uninject(player);
		}
		HandlerList.unregisterAll(listener);
		unregisterGlobalChannel();
	}

	private Object get(Object instance, String name) {
		try {
			Field field = instance.getClass().getDeclaredField(name);
			field.setAccessible(true);
			return field.get(instance);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private class PacketInjection extends ChannelDuplexHandler {

		public Player player;

		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
			if (msg instanceof PacketLoginInStart)
				playerChannel.put(((GameProfile) get(msg, "a")).getName(), ctx.channel());
			for (PacketHandler handel : handlerList.values()) {
				if (msg == null)
					break;
				msg = handel.onPacketIn(player, ctx.channel(), msg);
			}
			if (msg != null)
				super.channelRead(ctx, msg);
		}

		@Override
		public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
			for (PacketHandler handel : handlerList.values())
				msg = handel.onPacketOut(player, ctx.channel(), msg);
			super.write(ctx, msg, promise);
		}
	}
}
