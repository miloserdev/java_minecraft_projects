package real;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class Strike implements Listener {

	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player) {
			if (e.getCause() != EntityDamageEvent.DamageCause.PROJECTILE) {
				return;
			}
			Projectile projectile = (Projectile) e.getDamager();
			Player shooter = (Player) projectile.getShooter();
			if (shooter instanceof Player) {
				double projectileY = projectile.getLocation().getY();
				double damagedY = e.getEntity().getLocation().getY();
				if ((e.getEntity() instanceof Player)
						&& (projectileY - damagedY > 1.5D && projectileY - damagedY < 2.5D)) {
					shooter.sendMessage("HEADSHOT!");
					e.setDamage(0);
				}
			}
		}
	}

}
