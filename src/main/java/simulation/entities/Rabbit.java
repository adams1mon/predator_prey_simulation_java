package simulation.entities;

import config.Config;
import config.ConfigValue;
import di.DependencyContainer;
import simulation.entities.components.impl.DefaultDrawingComponent;
import simulation.entities.components.impl.DefaultPositionComponent;
import simulation.entities.components.impl.RabbitMoveComponent;
import simulation.field.Field;

import java.awt.*;

public class Rabbit extends Animal {

  public static final int ENERGY_LIMIT = 24;

  public Rabbit() {

    super(
        (DefaultDrawingComponent) DependencyContainer.getInstance(DefaultDrawingComponent.class),
        (RabbitMoveComponent) DependencyContainer.getInstance(RabbitMoveComponent.class),
        (DefaultPositionComponent) DependencyContainer.getInstance(DefaultPositionComponent.class)
    );
    super.color = Config.getColorProperty(ConfigValue.RABBIT_COLOR);
    super.energy = 20;
  }

  @Override
  public void spawnOffspring(Field field) {
    if (energy >= ENERGY_LIMIT) {
      var newPos = getNewPosition();
      var newX = newPos.getFirst();
      var newY = newPos.getSecond();

      if (!field.cellTaken(newX, newY)) {
        var rabbit = new Rabbit();
        energy /= 2;
        rabbit.setEnergy(energy);
        field.add(newX, newY, rabbit);
      }
    }
  }
}
