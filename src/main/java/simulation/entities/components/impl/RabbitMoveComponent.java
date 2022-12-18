package simulation.entities.components.impl;

import simulation.field.Field;
import simulation.entities.Animal;
import simulation.entities.components.MoveComponent;

public class RabbitMoveComponent implements MoveComponent {
  @Override
  public void move(Animal animal, Field field) {
    var oldPos = animal.getPosition();
    var oldX = oldPos.getFirst();
    var oldY = oldPos.getSecond();

    var newPos = animal.getNewPosition();
    var newX = newPos.getFirst();
    var newY = newPos.getSecond();

    if (!field.cellTaken(newX, newY)) {
      field.move(oldX, oldY, newX, newY);
      animal.setPosition(newX, newY);
    }
  }
}
