package gui;

import simulation.field.Field;
import simulation.field.Config;
import utils.LazyContainer;

import javax.swing.*;
import java.awt.*;

public class SimulationCanvas extends JPanel {

  private final Field field;

  public SimulationCanvas() {
    this.field = (Field) LazyContainer.getInstance(Field.class);
    var config = (Config) LazyContainer.getInstance(Config.class);

    var canvasWidth = config.getWidth() * config.getCellSize();
    var canvasHeight = config.getHeight() * config.getCellSize();

    setPreferredSize(new Dimension(canvasWidth, canvasHeight));
    setBounds(0, 0, canvasWidth, canvasHeight);

  }

  public Field getField() {
    return field;
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    field.simulate();
    field.draw(g);
  }
}
