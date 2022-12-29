package World;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.minecraft.server.v1_10_R1.EnumParticle;
import net.minecraft.server.v1_10_R1.PacketPlayOutWorldParticles;

public class XMAS extends BukkitRunnable {

	@Override
	public void run() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.FIREWORKS_SPARK,
					true, (float) p.getLocation().getX(), (float) p.getLocation().getY(),
					(float) p.getLocation().getZ(), 30, 50, 30, (float) 0, 300, null);
			((CraftPlayer) p).getHandle().playerConnection.networkManager.sendPacket(packet);
		}
	}

}
