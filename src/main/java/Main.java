import di.DependencyContainer;
import simulation.entities.components.impl.DefaultDrawingComponent;
import simulation.entities.components.impl.DefaultPositionComponent;
import simulation.entities.components.impl.FoxMoveComponent;
import simulation.entities.components.impl.RabbitMoveComponent;

import java.util.List;

public class Main {

  public static void main(String[] args) {

    // the 'entities' package doesn't use @Component and @Autowired
    // the config class should be instantiated before the field, because the 'entities' used by the field
    // need the config class too, but the container is unaware of this dependency,
    // so we need to take care of it manually

    List.of(
        new DefaultDrawingComponent(),
        new DefaultPositionComponent(),
        new FoxMoveComponent(),
        new RabbitMoveComponent()
    ).forEach(DependencyContainer::register);

    DependencyContainer.initializeContext();
  }
}
