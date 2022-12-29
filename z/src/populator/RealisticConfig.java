package populator;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;

public class RealisticConfig {
	public static boolean a = true;
	public static boolean b = true;
	public static boolean c = true;
	public static boolean d = true;
	public static boolean e = true;
	public static boolean f = false;
	public static boolean g = false;
	public static int h = 0;
	public static int i = 0;
	public static int j = 0;
	public static int k = 0;
	public static int l = 0;
	public static int m = 0;
	public static int n = 0;
	public static int o = 0;
	public static int p = 0;
	public static int q = 0;
	public static int r = 0;
	public static int s = 0;
	public static int t = 0;
	public static int u = 0;
	public static int v = 0;
	public static int w = 0;
	public static int x = 0;
	public static int y = 0;
	public static int z = 0;
	public static int A = 0;
	public static int B = 0;
	public static boolean C = false;
	public static File D = new File("plugins/RealisticWorld", "config.yml");
	public static FileConfiguration E = YamlConfiguration.loadConfiguration((File) D);

	public static void a() {
		if (!E.getBoolean("Biome.Vulcano")) {
			E.set("Biome.Vulcano", (Object) false);
			try {
				E.save(D);
			} catch (IOException var0) {
				var0.printStackTrace();
			}
		}
		f = E.getBoolean("Biome.Vulcano");
	}

	public static void b() {
		if (!E.getBoolean("Floor.Carpet")) {
			E.set("Floor.CarpetSet", (Object) "FloorDesign Set true or false");
			E.set("Floor.Carpet", (Object) false);
		}
		if (!E.getBoolean("Floor.Melone")) {
			E.set("Floor.MeloneSet", (Object) "FloorDesign Set true or false");
			E.set("Floor.Melone", (Object) false);
		}
		if (!E.getBoolean("Floor.Hay")) {
			E.set("Floor.HaySet", (Object) "FloorDesign Set true or false");
			E.set("Floor.Hay", (Object) false);
		}
		if (!E.getBoolean("Floor.Wheat")) {
			E.set("Floor.WheatSet", (Object) "FloorDesign Set true or false");
			E.set("Floor.Wheat", (Object) false);
		}
		try {
			E.save(D);
		} catch (IOException var0) {
			var0.printStackTrace();
		}
		a = E.getBoolean("Floor.Carpet");
		c = E.getBoolean("Floor.Melone");
		b = E.getBoolean("Floor.Hay");
		d = E.getBoolean("Floor.Wheat");
	}

	public static void c() {
		if (!E.getBoolean("Spawner.ConfigSet")) {
			E.set("Spawner.ConfigSet", (Object) true);
			E.set("Spawner.Enabled", (Object) true);
			try {
				E.save(D);
			} catch (IOException var0) {
				var0.printStackTrace();
			}
		}
		e = E.getBoolean("Spawner.Enabled");
	}

	public static void d() {
		if (!E.getBoolean("Spawner.Types.ConfigSet2")) {
			E.set("Spawner.Types.ConfigSet2", (Object) true);
			E.set("Spawner.Types.Creeper", (Object) true);
			E.set("Spawner.Types.Enderman", (Object) true);
			E.set("Spawner.Types.Zombie", (Object) true);
			E.set("Spawner.Types.Skeleton", (Object) true);
			E.set("Spawner.Types.Spider", (Object) true);
			E.set("Spawner.Types.CaveSpider", (Object) true);
			E.set("Spawner.Types.Slime", (Object) true);
			E.set("Spawner.Types.Ghast", (Object) true);
			E.set("Spawner.Types.ZombiePigman", (Object) true);
			E.set("Spawner.Types.Ghast", (Object) true);
			E.set("Spawner.Types.Silverfish", (Object) true);
			E.set("Spawner.Types.Blaze", (Object) true);
			E.set("Spawner.Types.MagmaCube", (Object) true);
			E.set("Spawner.Types.Bat", (Object) true);
			E.set("Spawner.Types.Witch", (Object) true);
			E.set("Spawner.Types.Endermite", (Object) true);
			E.set("Spawner.Types.Guardian", (Object) true);
			E.set("Spawner.Types.Pig", (Object) true);
			E.set("Spawner.Types.Sheep", (Object) true);
			E.set("Spawner.Types.Cow", (Object) true);
			E.set("Spawner.Types.Chicken", (Object) true);
			E.set("Spawner.Types.Squid", (Object) true);
			E.set("Spawner.Types.Wolf", (Object) true);
			E.set("Spawner.Types.MushroomCow", (Object) true);
			E.set("Spawner.Types.Ocelot", (Object) true);
			E.set("Spawner.Types.Horse", (Object) true);
			E.set("Spawner.Types.Rabbit", (Object) true);
			E.set("Spawner.Types.Villager", (Object) true);
			try {
				E.save(D);
			} catch (IOException var0) {
				var0.printStackTrace();
			}
		}
		if (E.getBoolean("Spawner.Types.Skeleton")) {
			Utils.a.add(EntityType.SKELETON);
		}
		if (E.getBoolean("Spawner.Types.Zombie")) {
			Utils.a.add(EntityType.ZOMBIE);
		}
		if (E.getBoolean("Spawner.Types.Enderman")) {
			Utils.a.add(EntityType.ENDERMAN);
		}
		if (E.getBoolean("Spawner.Types.Creeper")) {
			Utils.a.add(EntityType.CREEPER);
		}
		if (E.getBoolean("Spawner.Types.Spider")) {
			Utils.a.add(EntityType.SPIDER);
		}
		if (E.getBoolean("Spawner.Types.CaveSpider")) {
			Utils.a.add(EntityType.CAVE_SPIDER);
		}
		if (E.getBoolean("Spawner.Types.Slime")) {
			Utils.a.add(EntityType.SLIME);
		}
		if (E.getBoolean("Spawner.Types.Ghast")) {
			Utils.a.add(EntityType.GHAST);
		}
		if (E.getBoolean("Spawner.Types.Silverfish")) {
			Utils.a.add(EntityType.SILVERFISH);
		}
		if (E.getBoolean("Spawner.Types.Blaze")) {
			Utils.a.add(EntityType.BLAZE);
		}
		if (E.getBoolean("Spawner.Types.MagmaCube")) {
			Utils.a.add(EntityType.MAGMA_CUBE);
		}
		if (E.getBoolean("Spawner.Types.Bat")) {
			Utils.a.add(EntityType.BAT);
		}
		if (E.getBoolean("Spawner.Types.Witch")) {
			Utils.a.add(EntityType.WITCH);
		}
		if (E.getBoolean("Spawner.Types.Endermite")) {
			Utils.a.add(EntityType.ENDERMITE);
		}
		if (E.getBoolean("Spawner.Types.Guardian")) {
			Utils.a.add(EntityType.GUARDIAN);
		}
		if (E.getBoolean("Spawner.Types.Pig")) {
			Utils.a.add(EntityType.PIG);
		}
		if (E.getBoolean("Spawner.Types.Sheep")) {
			Utils.a.add(EntityType.SHEEP);
		}
		if (E.getBoolean("Spawner.Types.Cow")) {
			Utils.a.add(EntityType.COW);
		}
		if (E.getBoolean("Spawner.Types.Chicken")) {
			Utils.a.add(EntityType.CHICKEN);
		}
		if (E.getBoolean("Spawner.Types.Squid")) {
			Utils.a.add(EntityType.SQUID);
		}
		if (E.getBoolean("Spawner.Types.Wolf")) {
			Utils.a.add(EntityType.WOLF);
		}
		if (E.getBoolean("Spawner.Types.MushroomCow")) {
			Utils.a.add(EntityType.MUSHROOM_COW);
		}
		if (E.getBoolean("Spawner.Types.Ocelot")) {
			Utils.a.add(EntityType.OCELOT);
		}
		if (E.getBoolean("Spawner.Types.Horse")) {
			Utils.a.add(EntityType.HORSE);
		}
		if (E.getBoolean("Spawner.Types.Rabbit")) {
			Utils.a.add(EntityType.RABBIT);
		}
		if (E.getBoolean("Spawner.Types.Villager")) {
			Utils.a.add(EntityType.VILLAGER);
		}
	}

	public static void e() {
		if (!E.getBoolean("Fence.ConfigSet")) {
			E.set("Fence.ConfigSet", (Object) true);
			E.set("Fence.Enabled", (Object) true);
			try {
				E.save(D);
			} catch (IOException var0) {
				var0.printStackTrace();
			}
		}
		C = E.getBoolean("Fence.Enabled");
	}

	public static void f() {
		if (!E.getBoolean("Biome.DesertPlus")) {
			E.set("Biome.DesertPlus", (Object) false);
			System.out.println("Overwrite");
			try {
				E.save(D);
			} catch (IOException var0) {
				var0.printStackTrace();
			}
		}
		g = E.getBoolean("Biome.DesertPlus");
	}

	public static void g() {
		if (E.getString("Coal.Info") == null) {
			E.set("Coal.Info",
					(Object) "Set Coal.Chance to a int between 0 and 1000, Set Coal.HightMin and Coal.HightMax between 1 and 256");
			E.set("Coal.Chance", (Object) 25);
			E.set("Coal.HightMin", (Object) 1);
			E.set("Coal.HightMax", (Object) 128);
			try {
				E.save(D);
			} catch (IOException var0) {
				var0.printStackTrace();
			}
		}
		h = E.getInt("Coal.Chance");
		o = E.getInt("Coal.HightMin");
		v = E.getInt("Coal.HightMax");
	}

	public static void h() {
		if (E.getString("Iron.Info") == null) {
			E.set("Iron.Info",
					(Object) "Set Iron.Chance to a int between 0 and 1000, Set Iron.HightMin and Iron.HightMax between 1 and 256");
			E.set("Iron.Chance", (Object) 18);
			E.set("Iron.HightMin", (Object) 1);
			E.set("Iron.HightMax", (Object) 48);
			try {
				E.save(D);
			} catch (IOException var0) {
				var0.printStackTrace();
			}
		}
		i = E.getInt("Iron.Chance");
		p = E.getInt("Iron.HightMin");
		w = E.getInt("Iron.HightMax");
	}

	public static void i() {
		if (E.getString("Gold.Info") == null) {
			E.set("Gold.Info",
					(Object) "Set Gold.Chance to a int between 0 and 1000, Set Gold.HightMin and Gold.HightMax between 1 and 256");
			E.set("Gold.Chance", (Object) 12);
			E.set("Gold.HightMin", (Object) 1);
			E.set("Gold.HightMax", (Object) 25);
			try {
				E.save(D);
			} catch (IOException var0) {
				var0.printStackTrace();
			}
		}
		n = E.getInt("Gold.Chance");
		u = E.getInt("Gold.HightMin");
		B = E.getInt("Gold.HightMax");
	}

	public static void j() {
		if (E.getString("Lapis.Info") == null) {
			E.set("Lapis.Info",
					(Object) "Set Lapis.Chance to a int between 0 and 1000, Set Lapis.HightMin and Lapis.HightMax between 1 and 256");
			E.set("Lapis.Chance", (Object) 20);
			E.set("Lapis.HightMin", (Object) 1);
			E.set("Lapis.HightMax", (Object) 38);
			try {
				E.save(D);
			} catch (IOException var0) {
				var0.printStackTrace();
			}
		}
		m = E.getInt("Lapis.Chance");
		t = E.getInt("Lapis.HightMin");
		A = E.getInt("Lapis.HightMax");
	}

	public static void k() {
		if (E.getString("Redstone.Info") == null) {
			E.set("Redstone.Info",
					(Object) "Set Redstone.Chance to a int between 0 and 1000, Set Redstone.HightMin and Redstone.HightMax between 1 and 256");
			E.set("Redstone.Chance", (Object) 20);
			E.set("Redstone.HightMin", (Object) 1);
			E.set("Redstone.HightMax", (Object) 20);
			try {
				E.save(D);
			} catch (IOException var0) {
				var0.printStackTrace();
			}
		}
		j = E.getInt("Redstone.Chance");
		q = E.getInt("Redstone.HightMin");
		x = E.getInt("Redstone.HightMax");
	}

	public static void l() {
		if (E.getString("Emeralds.Info") == null) {
			E.set("Emeralds.Info",
					(Object) "Set Emeralds.Chance to a int between 0 and 1000, Set Emeralds.HightMin and Emeralds.HightMax between 1 and 256");
			E.set("Emeralds.Chance", (Object) 5);
			E.set("Emeralds.HightMin", (Object) 5);
			E.set("Emeralds.HightMax", (Object) 35);
			try {
				E.save(D);
			} catch (IOException var0) {
				var0.printStackTrace();
			}
		}
		l = E.getInt("Emeralds.Chance");
		s = E.getInt("Emeralds.HightMin");
		z = E.getInt("Emeralds.HightMax");
	}

	public static void m() {
		if (E.getString("Diamonds.Info") == null) {
			E.set("Diamonds.Info",
					(Object) "Set Diamonds.Chance to a int between 0 and 1000, Set Diamonds.HightMin and Diamonds.HightMax between 1 and 256");
			E.set("Diamonds.Chance", (Object) 2);
			E.set("Diamonds.HightMin", (Object) 1);
			E.set("Diamonds.HightMax", (Object) 15);
			try {
				E.save(D);
			} catch (IOException var0) {
				var0.printStackTrace();
			}
		}
		k = E.getInt("Diamonds.Chance");
		r = E.getInt("Diamonds.HightMin");
		y = E.getInt("Diamonds.HightMax");
	}
}
