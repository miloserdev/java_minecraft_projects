package A_Main;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import ru.tehkode.permissions.bukkit.PermissionsEx;

public class Tags implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		for (Player all : Bukkit.getOnlinePlayers()) {
			String l = Main.getPlugin().getConfig().getString("groups." + getGroup(all));
			String prefix = l.split(",,")[0];
			String suffix = l.split(",,")[1];
			Packets.sendTag(all, prefix, suffix, 0);
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Packets.sendTag(e.getPlayer(), null, null, 1);
	}

	public static String getGroup(Player player) {
		List<String> groups = PermissionsEx.getUser(player).getParentIdentifiers();
		if (groups.size() == 0) {
			return PermissionsEx.getPermissionManager().getDefaultGroups("def").toString();
		}
		return groups.get(0);
	}

	static void a() {
		new BukkitRunnable() {

			@Override
			public void run() {
				for (Player p : Bukkit.getOnlinePlayers()) {
					p.getAddress();
					//List<String> str = Main.getPlugin().getConfig().getStringList("groups." + getGroup(p));
					//String prefix = myMap.get(str).toString().split(",,")[0];
					//String suffix = myMap.get(str).toString().split(",,")[1];
					//Packets.sendTag(p, prefix, suffix, 2);
					//Bukkit.broadcastMessage(myMap.get(str).toString() + "");
				}
			}
		}.runTaskTimer(Main.getPlugin(), 0, 10);
	}

}
