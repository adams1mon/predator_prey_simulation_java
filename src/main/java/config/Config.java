package config;

import di.annotations.Autowired;
import di.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

@Component
public class Config {

  public static final String PROP_WIDTH = "width";
  public static final String PROP_HEIGHT = "height";
  public static final String PROP_RABBITS = "rabbits";
  public static final String PROP_FOXES = "foxes";
  public static final String PROP_CELL_SIZE = "canvasCellSize";
  public static final String PROP_GAME_LOOP_INTERVAL_MILLIS = "gameLoopIntervalMillis";

  private static final Logger log = LoggerFactory.getLogger(Config.class);

  private final Map<String, String> defaultProps = Map.ofEntries(
      Map.entry(PROP_WIDTH, "120"),
      Map.entry(PROP_HEIGHT, "80"),
      Map.entry(PROP_RABBITS, "50"),
      Map.entry(PROP_FOXES, "50"),
      Map.entry(PROP_CELL_SIZE, "10"),
      Map.entry(PROP_GAME_LOOP_INTERVAL_MILLIS, "50")
  );
  private final Properties properties = new Properties();
  public static String CONFIG_FILE = "field_config.properties";

  @Autowired
  public Config() {
    try {
      loadDefaults();
      properties.load(new FileInputStream(CONFIG_FILE));
    } catch (IOException e) {
      e.printStackTrace();
      log.error("{} file could not be read", CONFIG_FILE);
    }
  }

  private void loadDefaults() {
    log.info("loading default config");
    defaultProps.forEach(properties::setProperty);
  }

  public int getProperty(String name) {
    return Integer.parseInt(properties.getProperty(name));
  }

  public int getWidth() {
    return getProperty(PROP_WIDTH);
  }

  public int getHeight() {
    return getProperty(PROP_HEIGHT);
  }

  public int getRabbitCount() {
    return getProperty(PROP_RABBITS);
  }

  public int getFoxCount() {
    return getProperty(PROP_FOXES);
  }

  public int getCellSize() {
    return getProperty(PROP_CELL_SIZE);
  }

  public int getGameLoopInterval() {
    return getProperty(PROP_GAME_LOOP_INTERVAL_MILLIS);
  }
}
