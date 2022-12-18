package simulation.entities;

import simulation.field.Field;
import simulation.entities.components.impl.ObjectDrawingComponent;
import simulation.entities.components.impl.ObjectPositionComponent;
import simulation.entities.components.impl.RabbitMoveComponent;

import java.awt.*;

public class Rabbit extends Animal {

  public static final int BREED_TIME = 20;
  private int breedTime = BREED_TIME;

  public Rabbit() {
    super(
        new ObjectDrawingComponent(),
        new RabbitMoveComponent(),
        new ObjectPositionComponent()
    );
    super.color = Color.LIGHT_GRAY;
  }

  @Override
  public void spawnOffspring(Field field) {
    if (--breedTime <= 0) {
      breedTime = BREED_TIME;
      var newPos = getNewPosition(field);
      var newX = newPos.getFirst();
      var newY = newPos.getSecond();
      if (!field.cellTaken(newX, newY)) {
        var rabbit = new Rabbit();
        rabbit.setPosition(newX, newY);
        field.add(newX, newY, rabbit);
      }
    }
  }
}
