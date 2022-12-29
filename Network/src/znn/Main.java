package znn;

import org.bukkit.event.Listener;

public class Main extends Plugin implements Listener {

	private static Plugin plugin;

	@Override
	public void onEnable() {
		plugin = this;
		BungeeCord.getInstance().getPluginManager().registerListener(this, this);
		Tag.runnable();
	}

	@Override
	public void onDisable() {
		plugin = null;
	}

	public static String getMainFolder() {
		return plugin.getDataFolder().getAbsolutePath();
	}

	public static Plugin getPlugin() {
		return plugin;
	}

}