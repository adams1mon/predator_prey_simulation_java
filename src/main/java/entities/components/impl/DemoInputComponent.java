package entities.components.impl;

import entities.GameObject;
import entities.components.InputComponent;

import java.awt.event.KeyEvent;

public class DemoInputComponent implements InputComponent {

  @Override
  public void update(GameObject object, int e) {
    System.out.println("DemoInputComponent.update(): handling input on npc object");
  }
}
