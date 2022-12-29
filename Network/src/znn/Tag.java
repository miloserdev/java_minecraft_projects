package znn;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.bukkit.event.EventHandler;

public class Tag {

	static Scoreboard sc;

	@EventHandler
	public void onJoin(ServerConnectedEvent e) {
		sc.addTeam(new Team(e.getPlayer().getName()));
	}

	@EventHandler
	public void onJoin(ServerDisconnectEvent e) {
		sc.removeTeam(e.getPlayer().getName());
	}

	public static void runnable() {
		for (ProxiedPlayer all : BungeeCord.getInstance().getPlayers())
			BungeeCord.getInstance().getScheduler().schedule(Main.getPlugin(), new Runnable() {

				List<String> p = ConfigManager.getConfig()
						.getStringList("groups."
								+ BungeePerms.getInstance().getPermissionsManager().getUser(all.getName()).buildPrefix()
								+ ".prefix");
				List<String> s = ConfigManager.getConfig()
						.getStringList("groups."
								+ BungeePerms.getInstance().getPermissionsManager().getUser(all.getName()).buildSuffix()
								+ ".suffix");

				int i = 0;

				@Override
				public void run() {
					if (i != p.size()) {
						sc.getTeam(all.getName()).setPrefix(p.get(i));
						sc.getTeam(all.getName()).setSuffix(s.get(i));
						i++;
					} else {
						i = 0;
						sc.getTeam(all.getName()).setPrefix(p.get(i));
						sc.getTeam(all.getName()).setSuffix(s.get(i));
						i++;
					}
				}
			}, 1, 1, TimeUnit.SECONDS);
	}

}
