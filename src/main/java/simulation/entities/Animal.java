package simulation.entities;

import simulation.entities.components.DrawingComponent;
import simulation.entities.components.MoveComponent;
import simulation.entities.components.PositionComponent;
import simulation.field.Field;
import utils.Pair;

public abstract class Animal extends FieldEntity {

  public int energy;

  protected MoveComponent moveComponent;
  protected PositionComponent positionComponent;

  protected Animal(
      DrawingComponent drawingComponent,
      MoveComponent moveComponent,
      PositionComponent positionComponent
  ) {
    super(drawingComponent);
    this.moveComponent = moveComponent;
    this.positionComponent = positionComponent;
  }

  @Override
  public void move(Field field) {
    moveComponent.move(this, field);
  }

  @Override
  public void loseEnergy(Field field) {
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
