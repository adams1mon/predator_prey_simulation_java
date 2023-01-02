package simulation.entities.components;

import simulation.entities.Animal;
import simulation.field.Field;

public interface MoveComponent {
  void move(Animal animal, Field field);
}
