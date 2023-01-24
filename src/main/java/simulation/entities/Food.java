package simulation.entities;

import config.Config;
import config.ConfigValue;
import di.DependencyContainer;
import simulation.entities.components.impl.DefaultDrawingComponent;
import simulation.field.Field;

public class Food extends FieldEntity {

  public Food(Field field) {
    super(
        field,
        (DefaultDrawingComponent) DependencyContainer.getInstance(DefaultDrawingComponent.class)
    );
    super.color = Config.getColorProperty(ConfigValue.FOOD_COLOR);
  }
}
