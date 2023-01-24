package simulation.entities;

import config.Config;
import config.ConfigValue;
import di.DependencyContainer;
import simulation.entities.components.impl.DefaultDrawingComponent;
import simulation.entities.components.impl.DefaultPositionComponent;
import simulation.entities.components.impl.FoxMoveComponent;
import simulation.field.Field;

import java.awt.*;

public class Fox extends Animal {

  public static final int ENERGY_LIMIT = 30;
  public static final int ENERGY_PER_RABBIT = 10;

  public Fox() {
    super(
        (DefaultDrawingComponent) DependencyContainer.getInstance(DefaultDrawingComponent.class),
        (FoxMoveComponent) DependencyContainer.getInstance(FoxMoveComponent.class),
        (DefaultPositionComponent) DependencyContainer.getInstance(DefaultPositionComponent.class)
    );
    super.color = Config.getColorProperty(ConfigValue.FOX_COLOR);
    super.energy = 20;
  }

  /**
   * If the fox has enough energy it spawns offspring in a free neighboring cell.
   * The energy is split evenly between the parent and the child.
   */
  @Override
  public void spawnOffspring(Field field) {
    if (energy >= ENERGY_LIMIT) {
      var newPos = getNewPosition();
      var newX = newPos.getFirst();
      var newY = newPos.getSecond();

      if (!field.cellTaken(newX, newY)) {
        var fox = new Fox();
        energy /= 2;
        fox.setEnergy(energy);
        field.add(newX, newY, fox);
      }
    }
  }
}
