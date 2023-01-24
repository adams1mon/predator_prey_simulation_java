package simulation.entities;

import simulation.entities.components.DrawingComponent;
import simulation.field.Field;
import utils.Pair;

import java.awt.*;

public abstract class FieldEntity {

  public Color color;

  protected int x = 0;
  protected int y = 0;

  protected Field field;
  protected DrawingComponent drawingComponent;

  protected FieldEntity(
      Field field,
      DrawingComponent drawingComponent
  ) {
    this.field = field;
    this.drawingComponent = drawingComponent;
  }

  public Field getField() {
    return field;
  }

  public Pair<Integer, Integer> getPosition() {
    return new Pair<>(x, y);
  }

  public void setPosition(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public void draw(Graphics graphics) {
    drawingComponent.draw(this, graphics);
  }

  public void move() {}
  public void spawnOffspring() {}
  public void loseEnergy() {}
}
