package simulation.entities.components;

import simulation.entities.Animal;
import utils.Pair;

public interface PositionComponent {
  Pair<Integer, Integer> findNewPositionFromCurrent(Animal animal);
}
