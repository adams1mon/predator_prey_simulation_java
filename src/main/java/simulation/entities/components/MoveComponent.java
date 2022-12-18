package simulation.entities.components;

import simulation.field.Field;
import simulation.entities.Animal;

public interface MoveComponent {
  void move(Animal animal, Field field);
}
