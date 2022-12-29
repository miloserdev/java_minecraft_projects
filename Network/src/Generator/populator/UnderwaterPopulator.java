/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.Chunk
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.generator.BlockPopulator
 */
package Generator.populator;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

public class UnderwaterPopulator extends BlockPopulator {
	public void populate(World world, Random random, Chunk chunk) {
		int n = 0;
		while (n < 16) {
			int n2 = 0;
			while (n2 < 16) {
				int n3 = 128;
				while (chunk.getBlock(n, n3, n2).getType() == Material.AIR
						|| chunk.getBlock(n, n3, n2).getType() == Material.STATIONARY_WATER
						|| chunk.getBlock(n, n3, n2).getType() == Material.WATER
						|| chunk.getBlock(n, n3, n2).getTypeId() == 31 || chunk.getBlock(n, n3, n2).getTypeId() == 18
						|| chunk.getBlock(n, n3, n2).getTypeId() == 17) {
					--n3;
				}
				if ((chunk.getBlock(n, n3 + 1, n2).getType() == Material.STATIONARY_WATER
						|| chunk.getBlock(n, n3 + 1, n2).getType() == Material.WATER)
						&& chunk.getBlock(n, n3, n2).getType() != Material.STATIONARY_WATER
						&& chunk.getBlock(n, n3, n2).getType() != Material.WATER) {
					int n4 = random.nextInt(100);
					if (n4 > 60) {
						chunk.getBlock(n, n3, n2).setType(Material.SAND);
					} else if (n4 > 40) {
						chunk.getBlock(n, n3, n2).setType(Material.GRAVEL);
					} else if (n4 > 30) {
						chunk.getBlock(n, n3, n2).setTypeId(3);
						chunk.getBlock(n, n3, n2).setTypeId(1);
					} else if (n4 > 10) {
						chunk.getBlock(n, n3, n2).setType(Material.CLAY);
					} else if (n4 > 2) {
						chunk.getBlock(n, n3, n2).setType(Material.OBSIDIAN);
					} else {
						chunk.getBlock(n, n3, n2).setType(Material.COBBLESTONE);
					}
				}
				++n2;
			}
			++n;
		}
	}
}
