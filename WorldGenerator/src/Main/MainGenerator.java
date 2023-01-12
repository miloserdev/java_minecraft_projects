package Main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import populator.RealisticConfig;
import populator.TreePopulator;

public class MainGenerator extends ChunkGenerator {
	public List<BlockPopulator> getDefaultPopulators(World world) {
		ArrayList<BlockPopulator> arrayList = new ArrayList<BlockPopulator>();
		arrayList.add(new TreePopulator());
		// arrayList.add(new SnowPopulator());
		// arrayList.add(new BeachPopulator());
		// arrayList.add(new UnderwaterPopulator());
		// arrayList.add(new WaterDesignPopulator());
		return arrayList;
	}

	private int a(int n, int n2, int n3) {
		return (n * 16 + n3) * 128 + n2;
	}

	public byte[] generate(World world, Random random, int n, int n2) {
		byte[] arrby = new byte[32768];
		Random random2 = new Random(world.getSeed());
		SimplexOctaveGenerator simplexOctaveGenerator = new SimplexOctaveGenerator(random2, 8);
		SimplexOctaveGenerator simplexOctaveGenerator2 = new SimplexOctaveGenerator(random2, 10);
		SimplexOctaveGenerator simplexOctaveGenerator3 = new SimplexOctaveGenerator(random2, 12);
		double d1 = 64.0;
		double d2 = 64.0;
		double d3 = 84.0;
		simplexOctaveGenerator.setScale(1.0D / d1);
		simplexOctaveGenerator2.setScale(1.0D / d2);
		simplexOctaveGenerator3.setScale(1.0D / d3);
		int n3 = 0;
		while (n3 < 16) {
			int n4 = 0;
			while (n4 < 16) {
				arrby[this.a((int) n3, (int) 0, (int) n4)] = (byte) Material.BEDROCK.getId();
				double d4 = simplexOctaveGenerator.noise((double) (n3 + n * 16), (double) (n4 + n2 * 16), 0.5, 0.5)
						* 15.0;
				double d5 = simplexOctaveGenerator2.noise((double) (n3 + n * 16), (double) (n4 + n2 * 16), 0.5, 0.5)
						* 6.0;
				double d6 = simplexOctaveGenerator3.noise((double) (n3 + n * 16), (double) (n4 + n2 * 16), 0.5, 0.5)
						* 35.0;
				double d7 = Math.max(d4, d5);
				double d8 = Math.max(d7, d6);
				int n5 = n3 + n * 16;
				int n6 = n4 + n2 * 16;
				int n7 = 1;
				while ((double) n7 < 64.0 + d8) {
					int n8;
					int n9 = random.nextInt(60) + 1;
					boolean bl = false;
					if (n9 == 1) {
						n8 = random.nextInt(1000);
						if (n8 < RealisticConfig.h && n7 <= RealisticConfig.v && n7 >= RealisticConfig.o) {
							arrby[this.a((int) n3, (int) n7, (int) n4)] = (byte) Material.COAL_ORE.getId();
							bl = true;
						}
					} else if (n9 == 2) {
						n8 = random.nextInt(1000);
						if (n8 < RealisticConfig.i && n7 <= RealisticConfig.w && n7 >= RealisticConfig.p) {
							arrby[this.a((int) n3, (int) n7, (int) n4)] = (byte) Material.IRON_ORE.getId();
							bl = true;
						}
					} else if (n9 == 3) {
						n8 = random.nextInt(1000);
						if (n8 < RealisticConfig.n && n7 <= RealisticConfig.B && n7 >= RealisticConfig.u) {
							arrby[this.a((int) n3, (int) n7, (int) n4)] = (byte) Material.GOLD_ORE.getId();
							bl = true;
						}
					} else if (n9 == 4) {
						n8 = random.nextInt(1000);
						if (n8 < RealisticConfig.m && n7 <= RealisticConfig.A && n7 >= RealisticConfig.t) {
							arrby[this.a((int) n3, (int) n7, (int) n4)] = (byte) Material.LAPIS_ORE.getId();
							bl = true;
						}
					} else if (n9 == 5) {
						n8 = random.nextInt(1000);
						if (n8 < RealisticConfig.j && n7 <= RealisticConfig.x && n7 >= RealisticConfig.q) {
							arrby[this.a((int) n3, (int) n7, (int) n4)] = (byte) Material.REDSTONE_ORE.getId();
							bl = true;
						}
					} else if (n9 == 6) {
						n8 = random.nextInt(1000);
						if (n8 < RealisticConfig.l && n7 <= RealisticConfig.z && n7 >= RealisticConfig.s) {
							arrby[this.a((int) n3, (int) n7, (int) n4)] = (byte) Material.EMERALD_ORE.getId();
							bl = true;
						}
					} else if (n9 == 7) {
						n8 = random.nextInt(1000);
						if (n8 < RealisticConfig.k && n7 <= RealisticConfig.y && n7 >= RealisticConfig.r) {
							arrby[this.a((int) n3, (int) n7, (int) n4)] = (byte) Material.DIAMOND_ORE.getId();
							bl = true;
						}
					} else {
						n8 = random.nextInt(1000);
						if (n8 < 30) {
							arrby[this.a((int) n3, (int) n7, (int) n4)] = (byte) Material.COBBLESTONE.getId();
							bl = true;
						} else {
							arrby[this.a((int) n3, (int) n7, (int) n4)] = (byte) Material.STONE.getId();
							bl = true;
						}
					}
					if (!bl) {
						n8 = random.nextInt(1000);
						if (n8 < 30) {
							arrby[this.a((int) n3, (int) n7, (int) n4)] = (byte) Material.COBBLESTONE.getId();
							bl = true;
						} else {
							arrby[this.a((int) n3, (int) n7, (int) n4)] = (byte) Material.STONE.getId();
							bl = true;
						}
					}
					++n7;
				}
				arrby[this.a((int) n3, (int) (n7 - 3), (int) n4)] = random.nextInt(100) < 80
						? (byte) Material.DIRT.getId() : (byte) Material.COBBLESTONE.getId();
				arrby[this.a((int) n3, (int) (n7 - 2), (int) n4)] = random.nextInt(100) < 80
						? (byte) Material.DIRT.getId() : (byte) Material.COBBLESTONE.getId();
				arrby[this.a((int) n3, (int) (n7 - 1), (int) n4)] = random.nextInt(100) < 80
						? (byte) Material.DIRT.getId() : (byte) Material.COBBLESTONE.getId();
				arrby[this.a((int) n3, (int) n7, (int) n4)] = (byte) Material.GRASS.getId();
				++n4;
			}
			++n3;
		}
		return arrby;
	}
}