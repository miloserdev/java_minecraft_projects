package Generator;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import Generator.jnbt.ByteArrayTag;
import Generator.jnbt.CompoundTag;
import Generator.jnbt.IntTag;
import Generator.jnbt.NBTInputStream;
import Generator.jnbt.ShortTag;
import Generator.jnbt.Tag;

public class Schematic {

	public static void SyncPaste(World world, Location loc, File file, int rotation, boolean air) {
		try {
			FileInputStream stream = new FileInputStream(file);
			NBTInputStream nbtStream = new NBTInputStream(stream);
			CompoundTag schematicTag = (CompoundTag) nbtStream.readTag();
			stream.close();
			nbtStream.close();
			Map<String, Tag> schematic = schematicTag.getValue();
			short width = ShortTag.class.cast(schematic.get("Width")).getValue();
			short length = ShortTag.class.cast(schematic.get("Length")).getValue();
			short height = ShortTag.class.cast(schematic.get("Height")).getValue();
			int xOff = IntTag.class.cast(schematic.get("WEOffsetX")).getValue();
			int yOff = IntTag.class.cast(schematic.get("WEOffsetY")).getValue();
			int zOff = IntTag.class.cast(schematic.get("WEOffsetZ")).getValue();
			byte[] blocks = ByteArrayTag.class.cast(schematic.get("Blocks")).getValue();
			byte[] data = ByteArrayTag.class.cast(schematic.get("Data")).getValue();
			for (int x = 0; x < width; ++x) {
				for (int y = 0; y < height; ++y) {
					for (int z = 0; z < length; ++z) {
						int index = y * width * length + z * width + x;
						int newX = 0, newZ = 0;
						switch (rotation) {
						case 0:
							newX = z + zOff;
							newZ = x + xOff;
							break;
						case 90:
							newX = -z + -zOff;
							newZ = x + xOff;
							break;
						case 180:
							newX = -z + -zOff;
							newZ = -x + -xOff;
							break;
						case 360:
							newX = z + zOff;
							newZ = -x + -xOff;
							break;
						}
						Block block = new Location(world, newX + loc.getX(), yOff + loc.getY(), newZ + loc.getZ())
								.getBlock();
						if (air == false && Material.getMaterial(data[index]) != Material.AIR) {
							block.setTypeIdAndData(blocks[index] & 0xFF, data[index], false);
						} else {
						}
					}
				}
			}
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}

}