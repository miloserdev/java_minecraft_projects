package Generator.populator;

import java.io.File;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.scheduler.BukkitRunnable;

import A_Main.Main;
import Generator.Schematic;

public class TreePopulator extends BlockPopulator {

	@Override
	public void populate(World world, Random random, Chunk chunk) {

		int cX = chunk.getX() * 16;
		int cZ = chunk.getZ() * 16;
		int cXOff = cX + random.nextInt(10);
		int cZOff = cZ + random.nextInt(10);

		Location loc = new Location(world, cXOff, world.getHighestBlockYAt(cXOff, cZOff), cZOff);
		if (random.nextInt(100) < 20) {
			new BukkitRunnable() {
				@Override
				public void run() {
					Schematic.SyncPaste(world, loc, new File(Utils.Files.random("C:\\Trees")), 90, false);
					cancel();
				}
			}.runTaskTimer(Main.getPlugin(), 0L, 0L);
		}
	}
}
