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

public class BeachPopulator extends BlockPopulator {
	public void populate(World world, Random random, Chunk chunk) {
		int n = 0;
		while (n < 16) {
			int n2 = 0;
			while (n2 < 16) {
				int n4 = 128;
				while (chunk.getBlock(n, n4, n2).getType() == Material.AIR
						|| chunk.getBlock(n, n4, n2).getTypeId() == 18 || chunk.getBlock(n, n4, n2).getTypeId() == 17) {
					--n4;
				}
				if (chunk.getBlock(n, n4, n2).getType() == Material.STATIONARY_WATER
						&& (chunk.getBlock(n, n4, n2 + 1).getType() == Material.GRASS
								|| chunk.getBlock(n, n4, n2 - 1).getType() == Material.GRASS
								|| chunk.getBlock(n + 1, n4, n2).getType() == Material.GRASS
								|| chunk.getBlock(n - 1, n4, n2).getType() == Material.GRASS)
						&& (random.nextInt(100)) > 95) {
					int n5;
					int n6;
					if (chunk.getBlock(n, n4, n2 + 1).getType() == Material.GRASS) {
						chunk.getBlock(n, n4, n2 + 1).setType(Material.SAND);
						if (random.nextInt(20) > 12) {
							n6 = random.nextInt(2) + 1;
							n5 = 0;
							while (n5 < n6) {
								chunk.getBlock(n, n4 + ++n5, n2 + 1).setType(Material.SUGAR_CANE_BLOCK);
							}
						}
						if (chunk.getBlock(n, n4, n2 + 2).getType() == Material.GRASS) {
							chunk.getBlock(n, n4, n2 + 2).setType(Material.SAND);
						}
						if (chunk.getBlock(n + 1, n4, n2 + 2).getType() == Material.GRASS) {
							chunk.getBlock(n + 1, n4, n2 + 2).setType(Material.SAND);
						}
						if (chunk.getBlock(n - 1, n4, n2 + 2).getType() == Material.GRASS) {
							chunk.getBlock(n - 1, n4, n2 + 2).setType(Material.SAND);
						}
						if (chunk.getBlock(n + 1, n4, n2 + 1).getType() == Material.GRASS) {
							chunk.getBlock(n + 1, n4, n2 + 1).setType(Material.SAND);
						}
						if (chunk.getBlock(n - 1, n4, n2 + 1).getType() == Material.GRASS) {
							chunk.getBlock(n - 1, n4, n2 + 1).setType(Material.SAND);
						}
						if (chunk.getBlock(n + 2, n4, n2 + 1).getType() == Material.GRASS) {
							chunk.getBlock(n + 2, n4, n2 + 1).setType(Material.SAND);
						}
						if (chunk.getBlock(n - 2, n4, n2 + 1).getType() == Material.GRASS) {
							chunk.getBlock(n - 2, n4, n2 + 1).setType(Material.SAND);
						}
						if (chunk.getBlock(n + 3, n4, n2 + 1).getType() == Material.GRASS) {
							chunk.getBlock(n + 3, n4, n2 + 1).setType(Material.SAND);
						}
						if (chunk.getBlock(n - 3, n4, n2 + 1).getType() == Material.GRASS) {
							chunk.getBlock(n - 3, n4, n2 + 1).setType(Material.SAND);
						}
						if (chunk.getBlock(n - 4, n4, n2 + 1).getType() == Material.GRASS) {
							chunk.getBlock(n - 4, n4, n2 + 1).setType(Material.SAND);
						}
						if (chunk.getBlock(n - 5, n4, n2 + 1).getType() == Material.GRASS) {
							chunk.getBlock(n - 5, n4, n2 + 1).setType(Material.SAND);
						}
						if (chunk.getBlock(n + 1, n4, n2).getType() == Material.GRASS) {
							chunk.getBlock(n + 1, n4, n2).setType(Material.SAND);
							if (random.nextInt(20) > 12) {
								n6 = random.nextInt(2) + 1;
								n5 = 0;
								while (n5 < n6) {
									chunk.getBlock(n + 1, n4 + ++n5, n2).setType(Material.SUGAR_CANE_BLOCK);
								}
							}
						}
						if (chunk.getBlock(n - 1, n4, n2).getType() == Material.GRASS) {
							chunk.getBlock(n - 1, n4, n2).setType(Material.SAND);
							if (random.nextInt(20) > 12) {
								n6 = random.nextInt(2) + 1;
								n5 = 0;
								while (n5 < n6) {
									chunk.getBlock(n - 1, n4 + ++n5, n2).setType(Material.SUGAR_CANE_BLOCK);
								}
							}
						}
						if (chunk.getBlock(n + 2, n4, n2).getType() == Material.GRASS) {
							chunk.getBlock(n + 2, n4, n2).setType(Material.SAND);
							if (random.nextInt(20) > 12) {
								n6 = random.nextInt(2) + 1;
								n5 = 0;
								while (n5 < n6) {
									chunk.getBlock(n + 2, n4 + ++n5, n2).setType(Material.SUGAR_CANE_BLOCK);
								}
							}
						}
						if (chunk.getBlock(n - 2, n4, n2).getType() == Material.GRASS) {
							chunk.getBlock(n - 2, n4, n2).setType(Material.SAND);
							if (random.nextInt(20) > 12) {
								n6 = random.nextInt(2) + 1;
								n5 = 0;
								while (n5 < n6) {
									chunk.getBlock(n - 2, n4 + ++n5, n2).setType(Material.SUGAR_CANE_BLOCK);
								}
							}
						}
					} else if (chunk.getBlock(n, n4, n2 - 1).getType() == Material.GRASS) {
						chunk.getBlock(n, n4, n2 - 1).setType(Material.SAND);
						if (random.nextInt(20) > 12) {
							n6 = random.nextInt(2) + 1;
							n5 = 0;
							while (n5 < n6) {
								chunk.getBlock(n, n4 + ++n5, n2 - 1).setType(Material.SUGAR_CANE_BLOCK);
							}
						}
						if (chunk.getBlock(n, n4, n2 - 2).getType() == Material.GRASS) {
							chunk.getBlock(n, n4, n2 - 2).setType(Material.SAND);
						}
						if (chunk.getBlock(n + 1, n4, n2 - 2).getType() == Material.GRASS) {
							chunk.getBlock(n + 1, n4, n2 - 2).setType(Material.SAND);
						}
						if (chunk.getBlock(n - 1, n4, n2 - 2).getType() == Material.GRASS) {
							chunk.getBlock(n - 1, n4, n2 - 2).setType(Material.SAND);
						}
						if (chunk.getBlock(n + 1, n4, n2 - 1).getType() == Material.GRASS) {
							chunk.getBlock(n + 1, n4, n2 - 1).setType(Material.SAND);
						}
						if (chunk.getBlock(n - 1, n4, n2 - 1).getType() == Material.GRASS) {
							chunk.getBlock(n - 1, n4, n2 - 1).setType(Material.SAND);
						}
						if (chunk.getBlock(n + 2, n4, n2 - 1).getType() == Material.GRASS) {
							chunk.getBlock(n + 2, n4, n2 - 1).setType(Material.SAND);
						}
						if (chunk.getBlock(n - 2, n4, n2 - 1).getType() == Material.GRASS) {
							chunk.getBlock(n - 2, n4, n2 - 1).setType(Material.SAND);
						}
						if (chunk.getBlock(n + 1, n4, n2).getType() == Material.GRASS) {
							chunk.getBlock(n + 1, n4, n2).setType(Material.SAND);
							if (random.nextInt(20) > 12) {
								n6 = random.nextInt(2) + 1;
								n5 = 0;
								while (n5 < n6) {
									chunk.getBlock(n + 1, n4 + ++n5, n2).setType(Material.SUGAR_CANE_BLOCK);
								}
							}
						}
						if (chunk.getBlock(n - 1, n4, n2).getType() == Material.GRASS) {
							chunk.getBlock(n - 1, n4, n2).setType(Material.SAND);
							if (random.nextInt(20) > 12) {
								n6 = random.nextInt(2) + 1;
								n5 = 0;
								while (n5 < n6) {
									chunk.getBlock(n - 1, n4 + ++n5, n2).setType(Material.SUGAR_CANE_BLOCK);
								}
							}
						}
						if (chunk.getBlock(n + 2, n4, n2).getType() == Material.GRASS) {
							chunk.getBlock(n + 2, n4, n2).setType(Material.SAND);
							if (random.nextInt(20) > 12) {
								n6 = random.nextInt(2) + 1;
								n5 = 0;
								while (n5 < n6) {
									chunk.getBlock(n + 2, n4 + ++n5, n2).setType(Material.SUGAR_CANE_BLOCK);
								}
							}
						}
						if (chunk.getBlock(n - 2, n4, n2).getType() == Material.GRASS) {
							chunk.getBlock(n - 2, n4, n2).setType(Material.SAND);
							if (random.nextInt(20) > 12) {
								n6 = random.nextInt(2) + 1;
								n5 = 0;
								while (n5 < n6) {
									chunk.getBlock(n - 2, n4 + ++n5, n2).setType(Material.SUGAR_CANE_BLOCK);
								}
							}
						}
					} else if (chunk.getBlock(n + 1, n4, n2).getType() == Material.GRASS) {
						chunk.getBlock(n, n4, n2 + 1).getType();
					} else {
						chunk.getBlock(n, n4, n2 + 1).getType();
					}
				}
				++n2;
			}
			++n;
		}
	}
}
