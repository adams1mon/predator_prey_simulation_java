package gui;

import simulation.Field;

import javax.swing.*;
import java.awt.*;

public class SimulationCanvas extends JPanel {

  public static final int CELL_SIZE = 10;

  private final Field field;

  public SimulationCanvas(int fieldWidth, int fieldHeight, int rabbitCount, int foxCount) {
    setPreferredSize(new Dimension(fieldWidth * CELL_SIZE, fieldHeight * CELL_SIZE));
    setBounds(
        0,
        0,
        fieldWidth * CELL_SIZE,
        fieldHeight * CELL_SIZE
    );

    this.field = new Field(fieldWidth, fieldHeight, rabbitCount, foxCount);
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
