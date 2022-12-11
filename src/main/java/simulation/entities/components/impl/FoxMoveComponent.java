package simulation.entities.components.impl;

import simulation.Field;
import simulation.entities.Animal;
import simulation.entities.components.MoveComponent;

import static simulation.entities.Fox.ENERGY_PER_RABBIT;

public class FoxMoveComponent implements MoveComponent {

  /**
   * Foxes move randomly to neighboring cells that are free or occupied by rabbits.
   * If the cell to which the fox is moving is occupied by other rabbits it is consumed.
   * The energy of the shark increases by a predefined value.
   */
  @Override
  public void move(Animal animal, Field field) {
    var oldPos = animal.getPosition();
    var oldX = oldPos.getFirst();
    var oldY = oldPos.getSecond();

    var newPos = animal.getNewPosition(field);
    var newX = newPos.getFirst();
    var newY = newPos.getSecond();

    var rabbitOnCell = field.rabbitOnCell(newX, newY);
    if (field.cellTaken(newX, newY) && !rabbitOnCell)
      return;

    if (rabbitOnCell) {
      field.remove(newX, newY);
      animal.addEnergy(ENERGY_PER_RABBIT);
    }

    field.move(oldX, oldY, newX, newY);
    animal.setPosition(newX, newY);
  }
}
