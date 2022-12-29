package A_Main;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import Generator.MainGenerator;
import real.Death;
import real.Strike;

public class Main extends JavaPlugin implements Listener {

	public static Plugin plugin;

	@Override
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(this, this);
		plugin = this;
		saveDefaultConfig();
		Bukkit.getPluginManager().registerEvents(new Tags(), this);
		Bukkit.getPluginManager().registerEvents(new Strike(), this);
		Bukkit.getPluginManager().registerEvents(new Death(), this);
		Tags.a();
	}
	
	@Override
	public void onDisable() {
		plugin = null;
	}

	public ChunkGenerator getDefaultWorldGenerator(String string, String string2) {
		return new MainGenerator();
	}

	public static String getVersion() {
		return plugin.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
	}

	public static String getMainFolder() {
		return plugin.getDataFolder().getAbsolutePath();
	}

	public static Plugin getPlugin() {
		return plugin;
	}
}