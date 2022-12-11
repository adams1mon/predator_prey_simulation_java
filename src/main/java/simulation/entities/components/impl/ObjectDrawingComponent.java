package simulation.entities.components.impl;

import gui.SimulationCanvas;
import simulation.entities.Animal;
import simulation.entities.components.DrawingComponent;

import java.awt.*;

public class ObjectDrawingComponent implements DrawingComponent {
  @Override
  public void draw(Animal animal, Graphics graphics) {
    var pos = animal.getPosition();
    graphics.setColor(animal.color);
    graphics.fillRect(
        pos.getFirst() * SimulationCanvas.CELL_SIZE,
        pos.getSecond() * SimulationCanvas.CELL_SIZE,
        SimulationCanvas.CELL_SIZE,
        SimulationCanvas.CELL_SIZE
    );
  }
}
