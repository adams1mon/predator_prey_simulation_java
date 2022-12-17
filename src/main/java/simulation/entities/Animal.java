package simulation.entities;

import simulation.Field;
import simulation.entities.components.DrawingComponent;
import simulation.entities.components.MoveComponent;
import simulation.entities.components.PositionComponent;
import utils.Pair;

import java.awt.*;
import java.util.concurrent.Callable;

public abstract class Animal {

  public static final int ENERGY_DEFAULT = 20;

  public Color color;

  protected int x = 0;
  protected int y = 0;

  public int energy = ENERGY_DEFAULT;

  protected DrawingComponent drawingComponent;
  protected MoveComponent moveComponent;
  protected PositionComponent positionComponent;

  protected Animal(
      DrawingComponent drawingComponent,
      MoveComponent moveComponent,
      PositionComponent positionComponent
  ) {
    this.drawingComponent = drawingComponent;
    this.moveComponent = moveComponent;
    this.positionComponent = positionComponent;
  }

  public Pair<Integer, Integer> getPosition() {
    return new Pair<>(x, y);
  }

  public void setPosition(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public int getEnergy() {
    return energy;
  }

  public void setEnergy(int energy) {
    this.energy = energy;
  }

  public void addEnergy(int energy) {
    this.energy += energy;
  }

  public void draw(Graphics graphics) {
    drawingComponent.draw(this, graphics);
  }

  public void move(Field field) {
    moveComponent.move(this, field);
  }

  public Pair<Integer, Integer> getNewPosition(Field field) {
    return positionComponent.findNewPositionFromCurrent(this, field);
  }

  public void loseEnergy(Field field) {}
  public abstract void spawnOffspring(Field field);
}
