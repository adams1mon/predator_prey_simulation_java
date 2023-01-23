package simulation.entities;

import simulation.entities.components.DrawingComponent;
import simulation.field.Field;
import utils.Pair;

import java.awt.*;

public abstract class FieldEntity {

  public Color color;

  protected int x = 0;
  protected int y = 0;

  protected DrawingComponent drawingComponent;

  protected FieldEntity(
      DrawingComponent drawingComponent
  ) {
    this.drawingComponent = drawingComponent;
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

  public void move(Field field) {}
  public void spawnOffspring(Field field) {}
  public void loseEnergy(Field field) {}
}
