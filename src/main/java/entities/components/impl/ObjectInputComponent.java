package entities.components.impl;

import entities.GameObject;
import entities.components.InputComponent;

import java.awt.event.KeyEvent;

public class ObjectInputComponent implements InputComponent {

  @Override
  public void update(GameObject object, int key) {
    System.out.println("ObjectInputComponent.update(): handling input on object: updating velocity");
    switch (key) {
      case KeyEvent.KEY_LOCATION_LEFT -> object.velocity -= 1;
      case KeyEvent.KEY_LOCATION_RIGHT -> object.velocity += 1;
    }
  }
}
