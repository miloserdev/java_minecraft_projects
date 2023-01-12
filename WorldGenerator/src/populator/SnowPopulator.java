/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.Chunk
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.block.Biome
 *  org.bukkit.block.Block
 *  org.bukkit.generator.BlockPopulator
 */
package populator;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;

public class SnowPopulator extends BlockPopulator {
	public void populate(World world, Random random, Chunk chunk) {
		int n = 0;
		while (n < 16) {
			int n2 = 0;
			while (n2 < 16) {
				int n3 = 128;
				while (chunk.getBlock(n, n3, n2).getType() == Material.AIR
						|| chunk.getBlock(n, n3, n2).getTypeId() == 18 || chunk.getBlock(n, n3, n2).getTypeId() == 17
						|| chunk.getBlock(n, n3, n2).getTypeId() == 78) {
					--n3;
				}
				int n4 = random.nextInt(100);
				if (chunk.getBlock(n, n3, n2).getBiome() == Biome.ICE_MOUNTAINS
						|| chunk.getBlock(n, n3, n2).getBiome() == Biome.MUTATED_ICE_FLATS) {
					if (chunk.getBlock(n, n3, n2).getType() != Material.WATER
							&& chunk.getBlock(n, n3, n2).getType() != Material.STATIONARY_WATER) {
						if (n4 > 20) {
							chunk.getBlock(n, n3, n2).setType(Material.SNOW_BLOCK);
						} else {
							chunk.getBlock(n, n3, n2).setType(Material.PACKED_ICE);
						}
						if (n4 > 59) {
							chunk.getBlock(n, n3 - 1, n2).setType(Material.SNOW_BLOCK);
						} else {
							chunk.getBlock(n, n3 - 1, n2).setType(Material.PACKED_ICE);
						}
						if (n4 > 90) {
							chunk.getBlock(n, n3 - 2, n2).setType(Material.SNOW_BLOCK);
						} else if (n4 > 79) {
							chunk.getBlock(n, n3 - 2, n2).setType(Material.STONE);
						} else {
							chunk.getBlock(n, n3 - 2, n2).setType(Material.PACKED_ICE);
						}
					} else if (chunk.getBlock(n, n3, n2).getType() == Material.STATIONARY_WATER) {
						if (n4 > 40) {
							chunk.getBlock(n, n3, n2).setType(Material.STATIONARY_WATER);
						} else if (n4 > 50) {
							chunk.getBlock(n, n3, n2).setType(Material.PACKED_ICE);
						} else {
							chunk.getBlock(n, n3, n2).setType(Material.ICE);
						}
					}
				}
				++n2;
			}
			++n;
		}
	}
}
