package di;

import di.annotations.Autowired;
import di.annotations.Component;
import di.graph.Graph;
import di.graph.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
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
    var allClasses = PackageScanner.getClasses();
    var classes = getComponentClasses(allClasses);
    var graph = initializeDependencyGraph(classes);
    instantiateDependencies(graph);
  }

  private static Graph<Class<?>> initializeDependencyGraph(Collection<Class<?>> classes) {
    log.info("initializing dependency graph");

    var graph = new Graph<Class<?>>();
    var nodes = new HashMap<Class<?>, Node<Class<?>>>();

    classes.forEach(clazz -> nodes.put(clazz, new Node<>(clazz)));

    // discover dependencies
    nodes.forEach((clazz, node) -> {
      var constructor = getAutowiredConstructor(clazz);
      for (var param : constructor.getParameters()) {
        var paramType = param.getType();
        var className = clazz.getName();

        if (nodes.containsKey(paramType)) {
          log.info("registering dependency {} of {}", paramType, className);
          graph.addEdge(nodes.get(clazz), nodes.get(paramType));
          continue;
        }

        if (container.containsKey(paramType)) {
          log.info("dependency {} of class {} found, has been registered manually", paramType, className);
          continue;
        }

        throw new RuntimeException("dependency " + paramType + " of class " + className +
            " could not be satisfied (not annotated - class with @Component and constructor with @Autowired -" +
            " and not registered manually either)");
      }
    });
    return graph;
  }

  // tries to instantiate the objects in reverse topological order of dependencies
  private static void instantiateDependencies(Graph<Class<?>> graph) {
    log.info("instantiating dependencies according to the dependency graph");

    graph.getReverseTopologicalOrder()
        .forEach(node -> {
          var clazz = node.getContent();

          if (!container.containsKey(clazz)) {
            var constructor = getAutowiredConstructor(clazz);
            var args = createFunctionArgumentList(constructor);

            try {
              log.info("instantiating {}, constructor {}", clazz.getName(), constructor);
              container.put(clazz, constructor.newInstance(args));
            }  catch (InvocationTargetException | IllegalAccessException | InstantiationException e) {
              throw new RuntimeException("error while initializing class " + clazz.getName());
            }
          }
        });
  }

  private static Object[] createFunctionArgumentList(Executable function) {
    var params = function.getParameters();
    var args = new Object[params.length];
    for (var i = 0; i < params.length; ++i) {

      // look in the global container
      if (!container.containsKey(params[i].getType())) {
        throw new RuntimeException("constructor parameter " + params[i].getType() +
            " of class " + function.getDeclaringClass().getName() + " has not been instantiated: " +
            "not annotated with @Component and doesn't have an @Autowired constructor and " +
            "was also not registered manually");
      }

      args[i] = container.get(params[i].getType());
    }
    return args;
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
      throw new RuntimeException(clazz.getName() + " doesn't have an @Autowired annotated constructor");
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
}
