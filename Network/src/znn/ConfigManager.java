package znn;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.bukkit.configuration.file.YamlConfiguration;

public abstract class ConfigManager {

   private static File configFile = new File("config.yml");
   private static Configuration config;
   private static YamlConfiguration yaml;

   public static Configuration getConfig() {
      if(config == null) {
         loadConfig();
      }
    return config;
   }

   public static void loadConfig() {
      yaml = (YamlConfiguration)ConfigurationProvider.getProvider(YamlConfiguration.class);
      saveDefaultConfig();
      try {
         config = yaml.load(configFile);
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   public static void saveConfig() {
      try {
         yaml.save(config, configFile);
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   public static void saveDefaultConfig() {
      if(config == null) {
         try {
            if(!configFile.exists()) {
               InputStream ex = getResourceAsStream("config.yml");
               if(ex == null) {
                  throw new IllegalStateException("config.yml not found in jar file");
               }

               yaml.save(yaml.load(ex), configFile);
               loadConfig();
             }
         } catch (IOException e) {
            e.printStackTrace();
         }
      }
   }

   private static InputStream getResourceAsStream(String name) {
      return ConfigManager.class.getClassLoader().getResourceAsStream(name);
   }
}