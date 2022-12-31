package simulation.entities.components.impl;

import simulation.entities.Animal;
import simulation.entities.components.DrawingComponent;
import config.Config;
import utils.di.DependencyContainer;

import java.awt.*;

public class ObjectDrawingComponent implements DrawingComponent {

  private final int cellSize;

  public ObjectDrawingComponent() {
    var config = (Config) DependencyContainer.getInstance(Config.class);
    cellSize = config.getCellSize();
  }

  @Override
  public void draw(Animal animal, Graphics graphics) {
    var pos = animal.getPosition();
    graphics.setColor(animal.color);
    graphics.fillRect(
        pos.getFirst() * cellSize,
        pos.getSecond() * cellSize,
        cellSize,
        cellSize
    );
  }
}
