package simulation.entities;

import simulation.Field;
import simulation.entities.components.impl.FoxMoveComponent;
import simulation.entities.components.impl.ObjectDrawingComponent;
import simulation.entities.components.impl.ObjectPositionComponent;

import java.awt.*;

public class Fox extends Animal {

  public static final int ENERGY_LIMIT = 30;
  public static final int ENERGY_PER_RABBIT = 10;

  public Fox() {
    super(
        new ObjectDrawingComponent(),
        new FoxMoveComponent(),
        new ObjectPositionComponent()
    );
    super.color = Color.RED;
  }

  /**
   * If the fox has enough energy it spawns offspring in a free neighboring cell.
   * The energy is split evenly between the parent and the child.
   */
  @Override
  public void spawnOffspring(Field field) {
    if (energy >= ENERGY_LIMIT) {
      var newPos = getNewPosition(field);
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

  /**
   * Foxes lose a small fixed amount of energy with every time step.
   * A fox dies if its energy level drops to zero.
   */
  @Override
  public void loseEnergy() {
    --energy;
  }

  /**
   * Update the field based on this fox's state
   */
  @Override
  public void update(Field field) {
    if (energy <= 0) {
      field.remove(x, y);
    }
  }

//  @Override
//  public Fox clone() {
//    var fox = new Fox();
//    fox.setPosition(x, y);
//    return fox;
//  }
}
