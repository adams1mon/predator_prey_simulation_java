package simulation.entities.components.impl;

import config.Config;
import config.ConfigValue;
import di.annotations.Autowired;
import di.annotations.Component;
import simulation.entities.Animal;
import simulation.entities.components.PositionComponent;
import utils.Pair;

import java.util.List;
import java.util.Random;

@Component
public class DefaultPositionComponent implements PositionComponent {

  public static List<Pair<Integer, Integer>> DIRECTIONS = List.of(
    new Pair<>(-1, 0),
    new Pair<>(0, -1),
    new Pair<>(1, 0),
    new Pair<>(0, 1)
  );

  private final int fieldWidth;
  private final int fieldHeight;

  @Autowired
  public DefaultPositionComponent() {
    fieldWidth = Config.getIntProperty(ConfigValue.WIDTH);
    fieldHeight = Config.getIntProperty(ConfigValue.HEIGHT);
  }

  @Override
  public Pair<Integer, Integer> findNewPositionFromCurrent(Animal animal) {
    var random = new Random();
    var directionIndex = random.nextInt(DIRECTIONS.size());

    var pos = animal.getPosition();
    var x = pos.getFirst();
    var y = pos.getSecond();

    var newX = x + DIRECTIONS.get(directionIndex).getFirst();
    var newY = y + DIRECTIONS.get(directionIndex).getSecond();

    if (newX >= fieldWidth) {
      newX -= fieldWidth;
    } else if (newX < 0) {
      newX += fieldWidth;
    }

    if (newY >= fieldHeight) {
      newY -= fieldHeight;
    } else if (newY < 0) {
      newY += fieldHeight;
    }

    return new Pair<>(newX, newY);
  }
}
