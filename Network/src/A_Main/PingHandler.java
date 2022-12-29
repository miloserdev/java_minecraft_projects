package A_Main;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Random;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.mojang.authlib.GameProfile;

import Packets.PacketEvent;
import net.minecraft.server.v1_10_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_10_R1.PacketStatusOutServerInfo;
import net.minecraft.server.v1_10_R1.ServerPing;
import net.minecraft.server.v1_10_R1.ServerPing.ServerPingPlayerSample;

public class PingHandler implements Listener {
	
	

	@EventHandler
	public void onPing(PacketEvent e) throws Exception {
		if (e.getPacket() instanceof PacketStatusOutServerInfo) {
			ServerPing ping = new ServerPing();
			ServerPingPlayerSample playersample = new ServerPingPlayerSample(1, 1);
			GameProfile[] profile = {new GameProfile(UUID.randomUUID(), "§e§lTest")};
			playersample.a(profile);

			ping.setServerInfo(new ServerPing.ServerData("§a§o§nJoin Now!", -1));

			ping.setMOTD(ChatSerializer
					.a("{\"text\":\"               §aMars Network §c1.8/1.9/1.10\n                     §e§lOPEN SOURCE!"
							+ "" + "\"}"));
			ping.setFavicon(imageToBase64(new Random().nextInt(8) + 1));
			ping.setPlayerSample(playersample);

			e.setPacket(new PacketStatusOutServerInfo(ping));
		}
	}

	private String imageToBase64(int index) {
		StringBuilder sb = new StringBuilder();
		try {
			ImageInputStream iis = ImageIO.createImageInputStream(new File("C:\\icon.gif"));
			ImageReader reader = (ImageReader) ImageIO.getImageReadersByFormatName("gif").next();
			reader.setInput(iis, false);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(reader.read(index), "png", baos);
			baos.flush();
			byte[] iib = baos.toByteArray();
			baos.close();
			sb.append("data:image/png;base64,").append(StringUtils.newStringUtf8(Base64.encodeBase64(iib)));
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		return sb.toString();
	}

}