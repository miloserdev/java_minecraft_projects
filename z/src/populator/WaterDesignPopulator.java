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
package populator;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

public class WaterDesignPopulator extends BlockPopulator {
	public void populate(World world, Random random, Chunk chunk) {
		int n = 0;
		while (n < 16) {
			int n2 = 0;
			while (n2 < 16) {
				int n3;
				int n4 = 128;
				while (chunk.getBlock(n, n4, n2).getType() == Material.AIR) {
					--n4;
				}
				if ((chunk.getBlock(n, n4 - 1, n2).getType() == Material.STATIONARY_WATER
						|| chunk.getBlock(n, n4 - 1, n2).getType() == Material.WATER)
						&& chunk.getBlock(n, n4, n2).getType() != Material.STATIONARY_WATER
						&& chunk.getBlock(n, n4, n2).getType() != Material.WATER && (n3 = random.nextInt(100)) > 95) {
					chunk.getBlock(n, n4, n2).setType(Material.WATER_LILY);
				}
				if (!(chunk.getBlock(n, n4, n2).getType() != Material.STATIONARY_WATER
						&& chunk.getBlock(n, n4, n2).getType() != Material.WATER
						|| chunk.getBlock(n, n4 + 1, n2).getType() != Material.STATIONARY_WATER
								&& chunk.getBlock(n, n4 + 1, n2).getType() != Material.WATER
						|| chunk.getBlock(n, n4 + 1, n2).getType() != Material.STATIONARY_WATER
								&& chunk.getBlock(n, n4 + 1, n2).getType() != Material.WATER
						|| (n3 = random.nextInt(100)) <= 90)) {
					chunk.getBlock(n, n4, n2).setType(Material.LEAVES);
				}
				++n2;
			}
			++n;
		}
	}
}
