package Main;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import schematic.Schematic;

public class Main extends JavaPlugin implements Listener {

	public static Plugin plugin;
	
	public void onEnable() {
		plugin = this;
		Bukkit.getPluginManager().registerEvents(this, this);
		Bukkit.getPluginManager().registerEvents(new PacketInjector(), this);
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		Location loc = new Location(e.getPlayer().getWorld(), e.getPlayer().getLocation().getBlockX(), e.getPlayer().getLocation().getBlockY(), e.getPlayer().getLocation().getBlockZ());
		if (loc.getBlock().getType() != Material.AIR) {
				new BukkitRunnable() {
					@Override
					public void run() {
						try {
							Schematic.pasteSchematic(e.getPlayer().getWorld(), loc, new File("C:\\Trees\\" + random()), false);
							System.out.println("Pasted");
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						cancel();
					}

				}.runTaskTimer(Main.plugin, 0L, 0L);
		}
	}
	
	public String random() {
		String s = null;
		final File dir = new File("C:\\Trees");
		int size = dir.listFiles().length;
		String[] myArray = new String[size];
		File[] files = dir.listFiles();
		for (int i = 0; i < size; i++) {
			int idx = (int) (Math.random() * size);
			myArray[i] = files[idx].getName();
		}
		for (String x : myArray)
			s = x;
		return s;
	}

	public ChunkGenerator getDefaultWorldGenerator(String string, String string2) {
		return new MainGenerator();
	}

}
