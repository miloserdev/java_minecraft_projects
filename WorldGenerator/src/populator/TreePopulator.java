package populator;

import java.io.File;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.scheduler.BukkitRunnable;

import Main.Main;
import schematic.Schematic;

public class TreePopulator extends BlockPopulator {

	@Override
	public void populate(World world, Random random, Chunk chunk) {

		int cX = chunk.getX() * 16;
		int cZ = chunk.getZ() * 16;
		int cXOff = cX + random.nextInt(10);
		int cZOff = cZ + random.nextInt(10);

		Location loc = new Location(world, cXOff, world.getHighestBlockYAt(cXOff, cZOff), cZOff);
		if (loc.getBlock().getType() != Material.AIR) {
			if (random.nextInt(100) < 20) {
				new BukkitRunnable() {
					@Override
					public void run() {
						try {
							Schematic.pasteSchematic(world, loc, new File("C:\\Trees\\" + random()), false);
							System.out.println("Pasted");
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						cancel();
					}

				}.runTaskTimer(Main.plugin, 0L, 0L);
			}
		}
	}

	public String random() {
		String s = null;
		final File dir = new File("C:\\Trees");
		int size = dir.listFiles().length;
		String[] myArray = new String[size];
		File[] files = dir.listFiles();
		for (int i = 0; i < size; i++) {
			int idx = (int) (Math.random() * size);
			myArray[i] = files[idx].getName();
		}
		for (String x : myArray)
			s = x;
		return s;
	}

}
