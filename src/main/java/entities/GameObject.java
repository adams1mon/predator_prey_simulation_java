package entities;

import entities.components.GraphicsComponent;
import entities.components.InputComponent;
import entities.components.PhysicsComponent;
import entities.components.impl.DemoInputComponent;
import entities.components.impl.ObjectGraphicsComponent;
import entities.components.impl.ObjectInputComponent;
import entities.components.impl.ObjectPhysicsComponent;

public class GameObject {

  private final InputComponent inputComponent;
  private final PhysicsComponent physicsComponent;
  private final GraphicsComponent graphicsComponent;

  public String name;
  public int velocity = 0;
  public int coordinate = 0;

  public GameObject(
      InputComponent inputComponent,
      PhysicsComponent physicsComponent,
      GraphicsComponent graphicsComponent,
      String name
  ) {
    this.inputComponent = inputComponent;
    this.physicsComponent = physicsComponent;
    this.graphicsComponent = graphicsComponent;
    this.name = name;
  }

  public static GameObject createPlayer() {
    return new GameObject(
        new ObjectInputComponent(),
        new ObjectPhysicsComponent(),
        new ObjectGraphicsComponent(),
        "player"
    );
  }

  public static GameObject createNpc() {
    return new GameObject(
        new DemoInputComponent(),
        new ObjectPhysicsComponent(),
        new ObjectGraphicsComponent(),
        "npc"
    );
  }

  public void update(int key) {
    inputComponent.update(this, key);
    physicsComponent.update(this);
    graphicsComponent.render(this);
  }

  // probably only usable on 'npc' GameObjects
  public void demoUpdate() {
    // input key is ignored
    inputComponent.update(this, -1);
    physicsComponent.update(this);
    graphicsComponent.render(this);
  }
}
