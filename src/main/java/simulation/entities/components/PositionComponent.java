package simulation.entities.components;

import simulation.Field;
import simulation.entities.Animal;
import utils.Pair;

public interface PositionComponent {
  Pair<Integer, Integer> findNewPositionFromCurrent(Animal animal, Field field);
}
