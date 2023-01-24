package simulation.entities;

import config.Config;
import config.ConfigValue;
import di.DependencyContainer;
import simulation.entities.components.impl.DefaultDrawingComponent;
import simulation.entities.components.impl.DefaultPositionComponent;
import simulation.entities.components.impl.FoxMoveComponent;
import simulation.field.Field;

public class Fox extends Animal {

  public Fox(Field field) {
    super(
        field,
        (DefaultDrawingComponent) DependencyContainer.getInstance(DefaultDrawingComponent.class),
        (FoxMoveComponent) DependencyContainer.getInstance(FoxMoveComponent.class),
        (DefaultPositionComponent) DependencyContainer.getInstance(DefaultPositionComponent.class)
    );
    super.color = Config.getColorProperty(ConfigValue.FOX_COLOR);
    super.energy = 20;
    super.energyLimit = Config.getIntProperty(ConfigValue.FOX_ENERGY_LIMIT);
  }

  /**
   * If the fox has enough energy it spawns offspring in a free neighboring cell.
   * The energy is split evenly between the parent and the child.
   */
  @Override
  public void spawnOffspring() {
    if (energy >= energyLimit) {
      var newPos = getNewPosition();
      var newX = newPos.getFirst();
      var newY = newPos.getSecond();

      if (!field.cellTaken(newX, newY)) {
        var fox = new Fox(field);
        energy /= 2;
        fox.setEnergy(energy);
        field.add(newX, newY, fox);
      }
    }
  }
}
