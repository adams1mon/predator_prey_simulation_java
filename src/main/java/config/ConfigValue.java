package config;

public enum ConfigValue {

  WIDTH("120"),
  HEIGHT("80"),
  CELL_SIZE("10"),
  UPDATE_INTERVAL_MILLIS("50"), /** defines the lower lag limit (millis elapsed before an update) */
  UPDATE_INTERVAL_LIMIT_MILLIS("5"), /** pauses the "game loop" for this many seconds */
  STAT_UPDATE_INTERVAL_MILLIS("1000"),

  RABBIT_COUNT("50"),
  FOX_COUNT("50"),
  RABBIT_ENERGY_LIMIT("24"),
  FOX_ENERGY_LIMIT("30"),

  FOOD_SPAWN_CHANCE("0.1"),
  FOOD_ENERGY_CONTENT("10"),
  RABBIT_ENERGY_CONTENT("10"),

  RABBIT_COLOR("GRAY"),
  FOX_COLOR("RED"),
  FOOD_COLOR("ORANGE");

  public String value;

  ConfigValue(String defaultValue) {
    this.value = defaultValue;
  }
}
