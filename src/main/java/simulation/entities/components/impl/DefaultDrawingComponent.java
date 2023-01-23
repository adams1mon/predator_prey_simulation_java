package simulation.entities.components.impl;

import config.Config;
import config.ConfigValue;
import di.DependencyContainer;
import di.annotations.Autowired;
import di.annotations.Component;
import simulation.entities.Animal;
import simulation.entities.FieldEntity;
import simulation.entities.components.DrawingComponent;

import java.awt.*;

@Component
public class DefaultDrawingComponent implements DrawingComponent {

  private final int cellSize;

  @Autowired
  public DefaultDrawingComponent(Config config) {
    cellSize = config.getProperty(ConfigValue.CELL_SIZE);
  }

  @Override
  public void draw(FieldEntity entity, Graphics graphics) {
    var pos = entity.getPosition();
    graphics.setColor(entity.color);
    graphics.fillRect(
        pos.getFirst() * cellSize,
        pos.getSecond() * cellSize,
        cellSize,
        cellSize
    );
  }
}
