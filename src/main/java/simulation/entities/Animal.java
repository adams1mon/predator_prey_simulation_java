package simulation.entities;

import simulation.entities.components.DrawingComponent;
import simulation.entities.components.MoveComponent;
import simulation.entities.components.PositionComponent;
import simulation.field.Field;
import utils.Pair;

public abstract class Animal extends FieldEntity {

  protected int energy;
  protected int energyLimit;

  protected MoveComponent moveComponent;
  protected PositionComponent positionComponent;

  protected Animal(
      Field field,
      DrawingComponent drawingComponent,
      MoveComponent moveComponent,
      PositionComponent positionComponent
  ) {
    super(field, drawingComponent);
    this.moveComponent = moveComponent;
    this.positionComponent = positionComponent;
  }

  @Override
  public void move() {
    moveComponent.move(this);
  }

  @Override
  public void loseEnergy() {
    if (--energy <= 0) {
      field.remove(x, y);
    }
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

  public Pair<Integer, Integer> getNewPosition() {
    return positionComponent.findNewPositionFromCurrent(this);
  }
}
