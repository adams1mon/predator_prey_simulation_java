package gui;

import simulation.field.Field;
import config.Config;
import di.annotations.Autowired;
import di.annotations.Component;

import javax.swing.*;
import java.awt.*;

@Component
public class SimulationCanvas extends JPanel {

  private final Field field;

  @Autowired
  public SimulationCanvas(Config config, Field field) {
    this.field = field;

//    this.field = (Field) DependencyContainer.getInstance(Field.class);
//    var config = (Config) DependencyContainer.getInstance(Config.class);

    var canvasWidth = config.getWidth() * config.getCellSize();
    var canvasHeight = config.getHeight() * config.getCellSize();

    setPreferredSize(new Dimension(canvasWidth, canvasHeight));
    setBounds(0, 0, canvasWidth, canvasHeight);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    field.simulate();
    field.draw(g);
  }
}
