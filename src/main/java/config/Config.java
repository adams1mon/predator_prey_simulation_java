package config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {

  public static String CONFIG_FILE = "config.properties";
  private static final Logger log = LoggerFactory.getLogger(Config.class);
  private static final Properties PROPERTIES = new Properties();

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
  }

  public static int getIntProperty(ConfigValue value) {
    return Integer.parseInt(PROPERTIES.getProperty(value.name()));
  }

  public static double getDoubleProperty(ConfigValue value) {
    return Double.parseDouble(PROPERTIES.getProperty(value.name()));
  }

  public static Color getColorProperty(ConfigValue value) {
    try {
      log.info(Color.class.getField(PROPERTIES.getProperty(value.name())).get(null).toString());
      return (Color) Color.class.getField(PROPERTIES.getProperty(value.name())).get(null);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException("Error when trying to get color " + value.name(), e);
    }
  }
}
