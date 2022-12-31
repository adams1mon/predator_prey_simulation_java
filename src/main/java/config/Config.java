package config;

import utils.di.annotations.Autowired;
import utils.di.annotations.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

@Component
public class Config {

  public static final String PROP_WIDTH = "width";
  public static final String PROP_HEIGHT = "height";
  public static final String PROP_RABBITS = "rabbits";
  public static final String PROP_FOXES = "foxes";
  public static final String PROP_CELL_SIZE = "canvasCellSize";
  public static final String PROP_GAME_LOOP_INTERVAL_MILLIS = "gameLoopIntervalMillis";

  @Autowired
  private Logger log;

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

  public Config() {
    try {
      loadDefaults();
      properties.load(new FileInputStream(CONFIG_FILE));
    } catch (IOException e) {
      e.printStackTrace();
      System.err.println(CONFIG_FILE + " file could not be read!");
    }
  }

  private void loadDefaults() {
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
