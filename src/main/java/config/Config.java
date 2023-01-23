package config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

//@Component
public class Config {

  public static String CONFIG_FILE = "config.properties";

//  public static final String WIDTH = "width";
//  public static final String HEIGHT = "height";
//  public static final String CELL_SIZE = "cellSize";
//  public static final String UPDATE_INTERVAL_MILLIS = "updateIntervalMillis";
//
//
//  public static final String RABBITS = "rabbits";
//  public static final String FOXES = "foxes";

//  public static final String FOOD_ENERGY_CONTENT = 10;

  private static final Logger log = LoggerFactory.getLogger(Config.class);

//  private static final Map<String, String> DEFAULT_PROPS = Map.ofEntries(
//      Map.entry(WIDTH, "120"),
//      Map.entry(HEIGHT, "80"),
//      Map.entry(RABBITS, "50"),
//      Map.entry(FOXES, "50"),
//      Map.entry(CELL_SIZE, "10"),
//      Map.entry(UPDATE_INTERVAL_MILLIS, "50")
//  );
  private static final Properties PROPERTIES = new Properties();

  static {
    loadDefaults();
    loadFromFile();
  }

//  @Autowired
  public Config() {}

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

  WIDTH("120"),
  HEIGHT("80"),
  CELL_SIZE("10"),
  UPDATE_INTERVAL_MILLIS("50"),

  RABBITS("50"),
  FOXES("50"),
  RABBIT_ENERGY_LIMIT("24"),
  FOX_ENERGY_LIMIT("30"),

  FOOD_SPAWN_CHANCE("0.1"),
  FOOD_ENERGY_CONTENT("10"),
  RABBIT_ENERGY_CONTENT("10");

  public static int getProperty(ConfigValue value) {
    return Integer.parseInt(PROPERTIES.getProperty(value.name()));
  }

  public static int getWidth() {
    return getProperty(ConfigValue.WIDTH);
  }

  public static int getHeight() {
    return getProperty(ConfigValue.HEIGHT);
  }

  public static int getCellSize() {
    return getProperty(ConfigValue.CELL_SIZE);
  }

  public static int getRabbitCount() {
    return getProperty(RABBITS);
  }

  public static int getFoxCount() {
    return getProperty(FOXES);
  }



  public static int getGameLoopInterval() {
    return getProperty(UPDATE_INTERVAL_MILLIS);
  }
}
