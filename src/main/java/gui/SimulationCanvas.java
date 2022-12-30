package gui;

import simulation.field.Field;
import config.Config;
import utils.di.LazyContainer;

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

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    field.simulate();
    field.draw(g);
  }
}
