package simulation.entities;

import config.Config;
import config.ConfigValue;
import di.DependencyContainer;
import simulation.entities.components.impl.DefaultDrawingComponent;

public class Food extends FieldEntity {

  public Food() {
    super(
        (DefaultDrawingComponent) DependencyContainer.getInstance(DefaultDrawingComponent.class)
    );
    super.color = Config.getColorProperty(ConfigValue.FOOD_COLOR);
  }
}
