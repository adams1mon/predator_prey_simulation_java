package simulation.entities.components.impl;

import simulation.Field;
import simulation.entities.Animal;
import simulation.entities.components.PositionComponent;
import utils.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ObjectPositionComponent implements PositionComponent {

  public static ArrayList<Pair<Integer, Integer>> DIRECTIONS = new ArrayList<>(
      List.of(
          new Pair<>(-1, 0),
          new Pair<>(0, -1),
          new Pair<>(1, 0),
          new Pair<>(0, 1)
      )
  );

  @Override
  public Pair<Integer, Integer> findNewPositionFromCurrent(Animal animal, Field field) {
    var random = new Random();
    var directionIndex = random.nextInt(DIRECTIONS.size());

    var fieldWidth = field.getWidth();
    var fieldHeight = field.getHeight();

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
