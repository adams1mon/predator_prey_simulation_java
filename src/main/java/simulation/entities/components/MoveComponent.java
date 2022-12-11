package simulation.entities.components;

import simulation.Field;
import simulation.entities.Animal;

public interface MoveComponent {
  void move(Animal animal, Field field);
}
