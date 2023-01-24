package simulation.entities.components.impl;

import config.Config;
import config.ConfigValue;
import di.annotations.Autowired;
import di.annotations.Component;
import simulation.entities.Animal;
import simulation.entities.Food;
import simulation.entities.components.MoveComponent;
import simulation.field.Field;

@Component
public class RabbitMoveComponent implements MoveComponent {

  @Autowired
  public RabbitMoveComponent() {}

  /**
   * Rabbits move randomly to neighboring cells that are free or occupied by food.
   * The energy of the rabbit increases by a predefined value.
   */
  @Override
  public void move(Animal animal) {
    var oldPos = animal.getPosition();
    var oldX = oldPos.getFirst();
    var oldY = oldPos.getSecond();

    var newPos = animal.getNewPosition();
    var newX = newPos.getFirst();
    var newY = newPos.getSecond();

    var field = animal.getField();
    var entity = field.getEntity(newX, newY);

    if (entity != null) {
      if (entity instanceof Food) {
        field.remove(newX, newY);
        animal.addEnergy(Config.getIntProperty(ConfigValue.FOOD_ENERGY_CONTENT));
      } else {
        return;
      }
    }

    field.move(oldX, oldY, newX, newY);
    animal.setPosition(newX, newY);
  }
}
