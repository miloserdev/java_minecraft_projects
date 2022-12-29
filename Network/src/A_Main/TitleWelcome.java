package A_Main;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class TitleWelcome implements Listener {

	public static String[] str = { "W,, a, a,, 0,, 5,, 3", "We,, a,, 0,, 2,, 3", "Wel,, a,a ,, 0,, 2,, 3", "Welc,, a   ,, 1,, 5,, 3",
			"Welco,, a,, 0,, 2,, 3", "Welcom,, a,, 0,, 0,, 3", "Welcome,, a,, 1,, 2,, 10" };

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		new BukkitRunnable() {

			int i = str.length;
			int a = 0;
			// List<String> StringTitle =
			// Main.getInstance().getConfig().getStringList("Motd.Title");
			// List<String> StringSubTitle =
			// Main.getInstance().getConfig().getStringList("Motd.SubTitle");

			String thechar = ",";

			@Override
			public void run() {
				if (i != 0) {
					String msg1 = str[a].split(",,")[0].replace(thechar, ",");
					String msg2 = str[a].split(",,")[1].replace(thechar, ",");
					int in = new Integer(str[a].split(",,")[2].replace(" ", "")).intValue();
					int stay = new Integer(str[a].split(",,")[3].replace(" ", "")).intValue();
					int out = new Integer(str[a].split(",,")[4].replace(" ", "")).intValue();
					Packets.sendTitle(e.getPlayer(), ph(msg1, e.getPlayer()), ph(msg2, e.getPlayer()), in, stay, out);
					i--;
					a++;
				} else {
					cancel();
				}
			}
		}.runTaskTimer(Main.getPlugin(), 0L, 10L);
	}

	public String ph(String input, Player p) {
		return input.replace("&", "§").replace("Name", p.getName()).replace("Displayname", p.getDisplayName())
				.replace("Listname", p.getPlayerListName()).replace("Health", p.getHealth() + "")
				.replace("Food", p.getFoodLevel() + "").replace("Level", p.getLevel() + "")
				.replace("Exp", p.getExp() + "").replace("Playeradress", p.getAddress() + "")
				.replace("Version", Bukkit.getVersion()).replace("Bukkitversion", Bukkit.getBukkitVersion())
				.replace("Ip", Bukkit.getIp()).replace("Port", Bukkit.getPort() + "")
				.replace("Worldtime", p.getWorld().getTime() + "").replace("Maxplayers", Bukkit.getMaxPlayers() + "")
				.replace("Onlineplayers", Bukkit.getOnlinePlayers().size() + "");
	}

}