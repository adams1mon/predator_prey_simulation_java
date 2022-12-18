package entities.components.impl;

import entities.GameObject;
import entities.components.PhysicsComponent;

public class ObjectPhysicsComponent implements PhysicsComponent {

  @Override
  public void update(GameObject object) {
    System.out.println("ObjectPhysicsComponent.update(): updating position of the object");
    object.coordinate += object.velocity;
  }
}
