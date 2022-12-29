package A_Main;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class Chat implements Listener {

	boolean censor = true;
	boolean local = true;
	int radius = 10;

	@EventHandler
	public void a(AsyncPlayerChatEvent e) {
		e.setCancelled(true);
		String message = e.getMessage();
		String format = "<" + e.getPlayer().getName() + "> ";
		if (censor) {
			message = cen(message);
		}
		if (local) {
			for (Player all : Bukkit.getOnlinePlayers()) {
				if (!message.startsWith("!")) {
					for (Entity entity : e.getPlayer().getNearbyEntities(3, 3, 3)) {
						if (entity instanceof Player) {
							Player p = (Player) entity;
							p.sendMessage(format + message);
						}
					}
				} else {
					message = message.substring(1);
					all.sendMessage(format + message);
				}
			}
		}

	}

	public String cen(String msg) {
		List<String> list = Arrays.asList(new String[] { "сука", "блять", "блядь", "ебать", "ебало", "пидорас",
				"гондон", "уёбок", "уебок", "сучка", "еблан", "долбаёб", "долбоёб", "ублюдок", "хуй", "пизда" });
		StringBuilder sb = new StringBuilder();
		for (String word : msg.split(" ")) {
			if (list.contains(word.toLowerCase().replace(".", "").replace(",", "").replaceAll(" ", "")
					.replaceAll("(\\w)\\1+", "$1"))) {
				sb.append(new String(new char[word.length()]).replace("\0", "*") + " ");
			} else {
				sb.append(word + " ");
			}
		}
		return sb.toString();

	}

}
