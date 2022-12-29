package Packets;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import io.netty.channel.Channel;

import org.bukkit.event.Cancellable;

public class PacketEvent extends Event implements Cancellable {

	private Object packet;
	private Channel channel;
	private boolean cancelled;
	public boolean in;
	private static final HandlerList handlers = new HandlerList();
	
	public PacketEvent(Object packet, Channel channel) {
		this.packet = packet;
		this.channel = channel;
		if (packet.getClass().getSimpleName().startsWith("PacketPlayIn")) {
			this.in = true;
		} else {
			this.in = false;
		}
	}
	
	public Channel getChannel() {
		return this.channel;
	}

	public Object getPacket() {
		return this.packet;
	}

	public void setPacket(Object packet) {
		this.packet = packet;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean bln) {
		this.cancelled = bln;
	}

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}