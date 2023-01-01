package utils.di;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Pair;
import utils.di.annotations.Autowired;
import utils.di.annotations.Component;
import utils.graph.Graph;
import utils.graph.Node;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class DependencyContainer {

  private static final Logger log = LoggerFactory.getLogger(DependencyContainer.class);
  private static final HashMap<Class<?>, Object> container = new HashMap<>();

  public static void register(Object obj) {
    if(!container.containsKey(obj.getClass())) {
      log.info("registering {} instance", obj.getClass().getName());
      container.put(obj.getClass(), obj);
    }
  }

  public static Object getInstance(Class<?> clazz) {
    if (!container.containsKey(clazz)) {
      throw new RuntimeException(clazz.getName() + " instance does not exist in the container");
    }
    return container.get(clazz);
  }

  public static void initializeContext() {
    log.info("initializing context");
    container.putAll(initializeClasses());
//    resolveDependencies(container);
  }



  private static HashMap<Class<?>, Object> initializeClasses() {
    log.info("initializing classes");
    var container = new HashMap<Class<?>, Object>();
    var classes = getComponentClasses(PackageScanner.getClasses());

    var graph = new Graph<Class<?>>();
    var nodes = new HashMap<Class<?>, Node<Class<?>>>();

    classes.forEach(clazz -> {
      nodes.put(clazz, new Node<>(clazz));
    });

    nodes.forEach((clazz, node) -> {
      var constructor = getAutowiredConstructor(clazz);
      for (var param : constructor.getParameters()) {
        if (!nodes.containsKey(param.getType())) {
          throw new RuntimeException("dependency " + param.getType() + " of class " + clazz.getName() +
                  " could not be satisfied (maybe not annotated with Component?");
        }
        graph.addEdge(nodes.get(clazz), nodes.get(param.getType()));
      }
    });

    graph.getReverseTopologicalOrder()
            .forEach(node -> {
              // get the node's dependencies from the already initialized objects
              var args = new ArrayList<Class<?>>();
              graph.getNeighbours(node)
                      .forEach(neighbour -> {

                      });
            });

    classes
        .forEach(clazz -> {
          if (!container.containsKey(clazz)) {
            try {

              var constructor = getAutowiredConstructor(clazz);
              Arrays.stream(constructor.getParameters())
                              .forEach(parameter -> {

                              });


              container.put(clazz, constructor.newInstance());

            } catch (InvocationTargetException | IllegalAccessException | InstantiationException e) {
              log.error("error when trying to instantiate class {}", clazz.getName());
              throw new RuntimeException("error while initializing class " + clazz.getName());
            }
          }
        });
    return container;
  }

  private static List<Class<?>> getComponentClasses(Collection<Class<?>> classes) {
    return filterByAnnotation(classes, Component.class);
  }
//
//  private static void resolveDependencies(HashMap<Class<?>, Object> objects) {
//    log.info("resolving dependencies (class fields)");
//    getAutowiredClassFields(objects.values())
//        .forEach(pair -> {
//          var field = pair.getFirst();
//          var instance = pair.getSecond();
//          var clazz = instance.getClass();
//            if (objects.containsKey(field.getType())) {
//              field.setAccessible(true);
//              try {
//                log.info("setting field {} of class {}", field.getName(), clazz.getName());
//
//                field.set(instance, objects.get(field.getType()));
//
//              } catch (IllegalAccessException e) {
//                log.error("error while trying to set field {} of class {}", field.getName(), clazz.getName());
//                e.printStackTrace();
//              }
//            } else {
//              log.info("autowired field {} {} of class {} not found", field.getType(), field.getName(), clazz.getName());
//            }
//          });
//  }
//
//  private static Collection<Pair<Field, Object>> getAutowiredClassFields(Collection<Object> instances) {
//    var fields = new LinkedList<Pair<Field, Object>>();
//    instances.forEach(instance ->
//        filterByAnnotation(
//            List.of(instance.getClass().getDeclaredFields()),
//            Autowired.class
//        )
//          .forEach(field -> fields.add(new Pair<>(field, instance)))
//    );
//    return fields;
//  }

  private static Constructor<?> getAutowiredConstructor(Class<?> clazz) {
    var autowiredConstructors = filterByAnnotation(List.of(clazz.getConstructors()), Autowired.class);
    if (autowiredConstructors.isEmpty()) {
      log.error(
              "class {} doesn't have a public default constructor, skipping initialization: ",
              clazz.getName()
      );
      throw new RuntimeException(clazz.getName() + " doesn't have an 'Autowired' annotated constructor");
    }
    return autowiredConstructors.get(0);
  }

  private static <T extends AnnotatedElement> List<T> filterByAnnotation(
      Collection<T> elements,
      Class<? extends Annotation> annotation
  ) {
    return elements.stream()
        .filter(element -> element.isAnnotationPresent(annotation))
        .collect(Collectors.toList());
  }

  // 1. discover classes in classpath
  // 2. go through the annotated fields and build a dependency graph
  // 3. create topological order in the graph, signal cycles !!!
  // 4. instantiate the objects in topological order, injecting any dependencies in subsequent nodes..
}
