import org.slf4j.LoggerFactory;
import utils.di.DependencyContainer;
import utils.di.PackageScanner;

public class Main {
  public static void main(String[] args) throws Exception {

    System.out.println(Main.class.getPackageName());
    System.out.println(Main.class.getCanonicalName());
    System.out.println("hellooo");

//    var classes = PackageScanner.getComponentClasses();
//
//    classes.forEach(c -> System.out.println(c.getCanonicalName()));


    DependencyContainer.register(LoggerFactory.getLogger("default logger"));
    DependencyContainer.initializeContext();


//    Arrays.stream(ClassLoader.getSystemClassLoader().getDefinedPackages())
//        .forEach(System.out::println);

//    register(new Config());
//    register(new Field());
//    register(new SimulationCanvas());
//    register(new GameLoop());
//    register(new ControlPanel());
//    register(new StatisticsPanel());

//    new MainWindow("Something");
  }
}
