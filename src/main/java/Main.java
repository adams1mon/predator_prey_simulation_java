import config.Config;
import di.DependencyContainer;

public class Main {
  public static void main(String[] args) {

    // the 'entities' package doesn't use @Component and @Autowired
    // the config class should be instantiated before the field, because the 'entities' used by the field
    // need the config class too, but the container is unaware of this dependency,
    // so we need to take care of it manually

    DependencyContainer.register(new Config());
    DependencyContainer.initializeContext();
  }
}
