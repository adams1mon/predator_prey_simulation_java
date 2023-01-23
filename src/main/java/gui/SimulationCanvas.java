package gui;

import config.Config;
import config.ConfigValue;
import di.annotations.Autowired;
import di.annotations.Component;
import simulation.field.Field;

import javax.swing.*;
import java.awt.*;

@Component
public class SimulationCanvas extends JPanel {

  private final Field field;

  @Autowired
  public SimulationCanvas(Config config, Field field) {
    this.field = field;

    var canvasWidth = config.getProperty(ConfigValue.WIDTH) * config.getProperty(ConfigValue.CELL_SIZE);
    var canvasHeight = config.getProperty(ConfigValue.HEIGHT) * config.getProperty(ConfigValue.CELL_SIZE);

    setPreferredSize(new Dimension(canvasWidth, canvasHeight));
    setBounds(0, 0, canvasWidth, canvasHeight);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    field.draw(g);
    field.simulate();
  }
}
