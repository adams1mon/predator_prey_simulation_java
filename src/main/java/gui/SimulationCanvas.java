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
  public SimulationCanvas(Field field) {
    this.field = field;

    var canvasWidth = Config.getIntProperty(ConfigValue.WIDTH) * Config.getIntProperty(ConfigValue.CELL_SIZE);
    var canvasHeight = Config.getIntProperty(ConfigValue.HEIGHT) * Config.getIntProperty(ConfigValue.CELL_SIZE);

    setPreferredSize(new Dimension(canvasWidth, canvasHeight));
    setBounds(0, 0, canvasWidth, canvasHeight);
    setOpaque(true);
    setBackground(Config.getColorProperty(ConfigValue.BACKGROUND_COLOR));
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    field.draw(g);
  }

  public Field getField() {
    return field;
  }
}
