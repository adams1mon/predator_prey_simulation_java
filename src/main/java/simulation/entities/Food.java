package simulation.entities;

import di.DependencyContainer;
import simulation.entities.components.impl.DefaultDrawingComponent;

import java.awt.*;

public class Food extends FieldEntity {

  public static final int ENERGY_PER_FOOD = 10;

  public Food() {
    super(
        (DefaultDrawingComponent) DependencyContainer.getInstance(DefaultDrawingComponent.class)
    );
    super.color = Color.BLACK;
  }
}
