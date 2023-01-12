package Main;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

public class PacketHandler extends ChannelDuplexHandler {

	@Override
	public void channelRead(ChannelHandlerContext context, Object packet) throws Exception {
		System.out.println("\u001B[31mR : " + packet.getClass().getSimpleName() + "\u001B[0m");
		super.channelRead(context, packet);
	}

	@Override
	public void write(ChannelHandlerContext context, Object packet, ChannelPromise promise) throws Exception {
		System.out.println("\u001B[36mW : " + packet.getClass().getSimpleName() + "\u001B[0m");
		super.write(context, packet, promise);
	}

}