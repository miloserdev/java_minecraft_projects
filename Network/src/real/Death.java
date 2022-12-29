package real;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;

import A_Main.Main;

public class Death implements Listener {

	@EventHandler(priority = EventPriority.LOW)
	public void onDeath(PlayerDeathEvent e) {
		ghostBeforeDead(e);
		Corpse.spawn(e.getEntity());
		noRespawnMenu(e);
	}

	public void ghostBeforeDead(PlayerDeathEvent e) {
		if (e.getEntity() instanceof Player) {

			Location loc = new Location(e.getEntity().getWorld(), e.getEntity().getLocation().getX(),
					e.getEntity().getLocation().getY() - 2, e.getEntity().getLocation().getZ(),
					e.getEntity().getLocation().getYaw(), e.getEntity().getLocation().getPitch());

			ArmorStand as = e.getEntity().getWorld().spawn(loc, ArmorStand.class);

			ItemStack skull = new ItemStack(Material.SKULL_ITEM);

			ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
			LeatherArmorMeta cpl = (LeatherArmorMeta) chestplate.getItemMeta();
			cpl.setColor(Color.fromRGB(255, 255, 255));
			chestplate.setItemMeta(cpl);

			ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS, 1);
			LeatherArmorMeta legg = (LeatherArmorMeta) leggings.getItemMeta();
			legg.setColor(Color.fromRGB(255, 255, 255));
			leggings.setItemMeta(legg);

			ItemStack boots = new ItemStack(Material.LEATHER_BOOTS, 1);
			LeatherArmorMeta boot = (LeatherArmorMeta) boots.getItemMeta();
			boot.setColor(Color.fromRGB(255, 255, 255));
			boots.setItemMeta(boot);

			as.setRightArmPose(new EulerAngle(-0.5, 0, 0));
			as.setLeftArmPose(new EulerAngle(-0.3, 0, 0));

			as.setRightLegPose(new EulerAngle(0.3, 0, 0));
			as.setLeftLegPose(new EulerAngle(0.5, 0, 0));

			/** as.setCustomNameVisible(true); */
			/** as.setCustomName(e.getEntity().getName()); */

			as.setArms(true);
			as.setGravity(false);
			as.setBasePlate(false);
			as.setHelmet(skull);
			as.setChestplate(chestplate);
			as.setLeggings(leggings);
			as.setBoots(boots);

			new BukkitRunnable() {
				int i = 0;

				@Override
				public void run() {
					if (i != 30) {
						i++;
						as.teleport(as.getLocation().add(0, 0.3, 0));
					} else {
						as.remove();
						cancel();
					}
				}
			}.runTaskTimerAsynchronously(Main.getPlugin(), 0, 1);
		}
	}

	public void noRespawnMenu(PlayerDeathEvent e) {
		Player victim = e.getEntity();
		Location loc = victim.getWorld().getSpawnLocation();
		Location spawn = new Location(e.getEntity().getWorld(), loc.getX(), loc.getY(), loc.getZ());
		
		victim.setHealth(20);
		victim.setFoodLevel(20);
		victim.setFireTicks(0);
		victim.teleport(spawn);
	}

}
