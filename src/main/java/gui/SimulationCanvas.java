package gui;

import simulation.Field;
import utils.LazyContainer;

import javax.swing.*;
import java.awt.*;

public class SimulationCanvas extends JPanel {

  public static final int CELL_SIZE = 10;

  private final Field field;

  public SimulationCanvas() {
    this.field = (Field) LazyContainer.getInstance(Field.class);
    var canvasWidth = field.getWidth() * CELL_SIZE;
    var canvasHeight = field.getHeight() * CELL_SIZE;
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
