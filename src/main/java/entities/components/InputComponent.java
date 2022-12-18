package entities.components;

import entities.GameObject;

public interface InputComponent {

  void update(GameObject object, int key);
}
