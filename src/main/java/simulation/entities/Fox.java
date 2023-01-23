package simulation.entities;

import di.DependencyContainer;
import simulation.entities.components.impl.FoxMoveComponent;
import simulation.entities.components.impl.DefaultDrawingComponent;
import simulation.entities.components.impl.DefaultPositionComponent;
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
    super.color = Color.RED;
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
        fox.setPosition(newX, newY);
        energy /= 2;
        fox.setEnergy(energy);
        field.add(newX, newY, fox);
      }
    }
  }
}
