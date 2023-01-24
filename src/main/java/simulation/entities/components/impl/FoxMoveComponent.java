package simulation.entities.components.impl;

import di.annotations.Autowired;
import di.annotations.Component;
import simulation.entities.Animal;
import simulation.entities.Food;
import simulation.entities.Rabbit;
import simulation.entities.components.MoveComponent;
import simulation.field.Field;

import static simulation.entities.Fox.ENERGY_PER_RABBIT;

@Component
public class FoxMoveComponent implements MoveComponent {

  @Autowired
  public FoxMoveComponent() {}

  /**
   * Foxes move randomly to neighboring cells that are free or occupied by rabbits.
   * If the cell to which the fox is moving is occupied by other rabbits it is consumed.
   * The energy of the fox increases by a predefined value.
   */
  @Override
  public void move(Animal animal, Field field) {
    var oldPos = animal.getPosition();
    var oldX = oldPos.getFirst();
    var oldY = oldPos.getSecond();

    var newPos = animal.getNewPosition();
    var newX = newPos.getFirst();
    var newY = newPos.getSecond();

    var entity = field.getEntity(newX, newY);

    if (entity != null) {
      if (entity instanceof Rabbit) {
        field.remove(newX, newY);
        animal.addEnergy(ENERGY_PER_RABBIT);
      } else if (entity instanceof Food) {
        field.remove(newX, newY);
      } else {
        return;
      }
    }

    field.move(oldX, oldY, newX, newY);
    animal.setPosition(newX, newY);
  }
}
