package main;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

	public static Plugin plugin;

	@Override
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(this, this);
		plugin = this;
		
		saveDefaultConfig();
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		
	}
	
	@Override
	public void onDisable() {
		plugin = null;
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