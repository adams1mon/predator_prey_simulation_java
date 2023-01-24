package config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import java.util.function.Function;

public class Config {

  public static String CONFIG_FILE = "config.properties";
  private static final Logger log = LoggerFactory.getLogger(Config.class);
  private static final Properties PROPERTIES = new Properties();
  private static final HashMap<ConfigValue, Object> CACHE = new HashMap<>();

  static {
    loadDefaults();
    loadFromFile();
  }

  private static void loadDefaults() {
    log.info("loading default config");
    for (ConfigValue v : ConfigValue.values()) {
      PROPERTIES.setProperty(v.name(), v.value);
    }
  }

  private static void loadFromFile() {
    try {
      PROPERTIES.load(new FileInputStream(CONFIG_FILE));
    } catch (IOException e) {
      e.printStackTrace();
      log.warn("{} file could not be read, using default configs", CONFIG_FILE);
    }
  }

  public static String getProperty(ConfigValue value) {
    return PROPERTIES.getProperty(value.name());
  }

  public static void setProperty(ConfigValue key, String value) {
    PROPERTIES.put(key.name(), value);
    CACHE.remove(key);
  }

  public static int getIntProperty(ConfigValue key) {
    return cacheIfNotPresent(key, (Integer::parseInt));
  }

  public static double getDoubleProperty(ConfigValue key) {
    return cacheIfNotPresent(key, Double::parseDouble);
  }

  public static Color getColorProperty(ConfigValue key) {
    return cacheIfNotPresent(key, s -> {
      try {
        return (Color) Color.class.getField(PROPERTIES.getProperty(key.name())).get(null);
      } catch (NoSuchFieldException | IllegalAccessException e) {
        throw new RuntimeException("Error when trying to get color " + key.name(), e);
      }
    });
  }

  private static <T> T cacheIfNotPresent(ConfigValue key, Function<String, T> getValue) {
    var obj = CACHE.get(key);
    if (obj == null) {
      var value = getValue.apply(PROPERTIES.getProperty(key.name()));
      CACHE.put(key, value);
      return value;
    }
    return (T) obj;
  }
}
