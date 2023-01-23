package simulation.entities.components;

import simulation.entities.FieldEntity;

import java.awt.*;

public interface DrawingComponent {
  void draw(FieldEntity object, Graphics graphics);
}
