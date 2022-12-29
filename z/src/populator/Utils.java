/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.EntityType
 */
package populator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.entity.EntityType;

public class Utils {
	public static List<EntityType> a = new ArrayList<EntityType>();

	public static EntityType a() {
		Random random = new Random();
		int n = random.nextInt(a.size());
		return a.get(n);
	}
}
