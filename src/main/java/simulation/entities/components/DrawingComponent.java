package simulation.entities.components;

import simulation.entities.Animal;

import java.awt.*;

public interface DrawingComponent {
  void draw(Animal animal, Graphics graphics);
}
