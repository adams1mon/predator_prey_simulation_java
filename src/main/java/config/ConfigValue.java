package config;

public enum ConfigValue {

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

  public String value;

  ConfigValue(String defaultValue) {
    this.value = defaultValue;
  }

}
