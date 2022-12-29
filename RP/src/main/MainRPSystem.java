package main;

import org.bukkit.entity.Player;

public class MainRPSystem {
	
	public static void addPlayer(Player p) {
		Main.getPlugin().getConfig().addDefault("players", p.getUniqueId());
		Main.getPlugin().getConfig().addDefault("players" + p.getUniqueId(), "name");
		Main.getPlugin().getConfig().addDefault("players" + p.getUniqueId(), "level");
		//Main.getPlugin().getConfig().set("players" + p.getUniqueId(), "level", );
	}
	
	public static void setLevel(Player p) {
		p.setLevel(Main.getPlugin().getConfig().getInt("players" + p.getUniqueId() + "level"));
	}

}
