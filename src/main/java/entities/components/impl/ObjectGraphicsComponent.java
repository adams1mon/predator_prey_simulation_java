package entities.components.impl;

import entities.GameObject;
import entities.components.GraphicsComponent;

public class ObjectGraphicsComponent implements GraphicsComponent {

  @Override
  public void render(GameObject object) {
    System.out.println("ObjectGraphicsComponent.render(): rendering object");
    // render based on object.coordinate
  }
}
