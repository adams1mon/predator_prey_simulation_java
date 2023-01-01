import org.slf4j.LoggerFactory;
import utils.di.DependencyContainer;
import utils.di.PackageScanner;
import utils.graph.Graph;
import utils.graph.Node;

public class Main {
  public static void main(String[] args) throws Exception {

    System.out.println(Main.class.getPackageName());
    System.out.println(Main.class.getCanonicalName());
    System.out.println("hellooo");

//    var classes = PackageScanner.getComponentClasses();
//
//    classes.forEach(c -> System.out.println(c.getCanonicalName()));


    DependencyContainer.register(LoggerFactory.getLogger("default logger"));
//    DependencyContainer.initializeContext();


    var graph = new Graph<Integer>();

    var node1= new Node<>(1);
    var node2= new Node<>(2);
    var node3= new Node<>(3);
    var node4= new Node<>(4);
    var node5= new Node<>(5);
    var node6= new Node<>(6);

    graph.addEdge(node1, node3);
    graph.addEdge(node2, node3);
    graph.addEdge(node3, node4);
    graph.addEdge(node4, node5);

    for (var node : graph.getReverseTopologicalOrder()) {
      System.out.println(node.getContent());
    }


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
