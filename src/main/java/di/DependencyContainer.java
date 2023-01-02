package di;

import di.annotations.Autowired;
import di.annotations.Bean;
import di.annotations.Component;
import di.graph.Graph;
import di.graph.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
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
    var allClasses = PackageScanner.getClasses();
    var classes = getComponentClasses(allClasses);
    var graph = initializeDependencyGraph(classes);
    instantiateDependencies(graph);
  }

  private static Graph<Class<?>> initializeDependencyGraph(Collection<Class<?>> classes) {
    log.info("initializing dependency graph");

    var graph = new Graph<Class<?>>();

    var nodes = new HashMap<Class<?>, Node<Class<?>>>();
    var beanMethodReturnTypes = new HashMap<Class<?>, Node<Class<?>>>();

    classes.forEach(clazz -> {
      var node = new Node<Class<?>>(clazz);
      nodes.put(clazz, node);
      getBeanMethods(clazz).forEach(method -> beanMethodReturnTypes.put(method.getReturnType(), node));
    });

    // discover dependencies
    nodes.forEach((clazz, node) -> {
      var constructor = getAutowiredConstructor(clazz);
      for (var param : constructor.getParameters()) {
        var paramType = param.getType();
        var className = clazz.getName();

        if (beanMethodReturnTypes.containsKey(paramType)) {
          log.info("registering dependency {} of {} (from @Bean method)", paramType, className);
          graph.addEdge(nodes.get(clazz), beanMethodReturnTypes.get(paramType));
          continue;
        }

        if (nodes.containsKey(paramType)) {
          log.info("registering dependency {} of {}", paramType, className);
          graph.addEdge(nodes.get(clazz), nodes.get(paramType));
          continue;
        }

        if (container.containsKey(paramType)) {
          log.info("dependency {} of class {} found, has been registered manually", paramType, className);
          continue;
        }

        log.info("dependency {} of class {} should be registered manually via a @Bean method " +
                "(or via a register() call before calling initializeContext() - not recommended, " +
                "would overwrite @Bean methods)",
            paramType,
            className
        );
      }
    });
    return graph;
  }

  /**
   * tries to instantiate the objects in reverse topological order of dependencies
   */
  private static void instantiateDependencies(Graph<Class<?>> graph) {
    log.info("instantiating dependencies according to the dependency graph");

    graph.getReverseTopologicalOrder()
        .forEach(node -> {
          var clazz = node.getContent();

          // instances can be registered manually or by @Bean methods beforehand,
          // so we ignore classes that have already been instantiated
          if (!container.containsKey(clazz)) {
            try {
              var instance = instantiateClass(clazz);
              container.put(clazz, instance);
              container.putAll(invokeBeanMethods(instance));
            }  catch (InvocationTargetException | IllegalAccessException | InstantiationException e) {
              throw new RuntimeException("error while initializing class " + clazz.getName());
            }
          }
        });
  }

  private static Object instantiateClass(Class<?> clazz)
      throws InvocationTargetException, InstantiationException, IllegalAccessException
  {
    var constructor = getAutowiredConstructor(clazz);
    var args = createFunctionArgumentList(constructor);
    log.info("instantiating {}, constructor {}", clazz.getName(), constructor);
    return constructor.newInstance(args);
  }

  /**
   * creates objects from the defined bean methods in a class
   * bean methods can not have any parameters as of now.
   * it would be nicer to create the dependency graph using the bean method parameters too,
   * but that would complicate things and currently those dependencies can be declared as
   * class fields and injected via the @Autowired constructor, which also configures the dependency graph
   */
  private static Map<Class<?>, Object> invokeBeanMethods(Object instance)
      throws InvocationTargetException, IllegalAccessException
  {
    var beanObjects = new HashMap<Class<?>, Object>();
    for (var method : getBeanMethods(instance.getClass())) {
      log.info("invoking @Bean method {} of class {}", method.getName(), instance.getClass().getName());
      beanObjects.put(method.getReturnType(), method.invoke(instance));
    }
    return beanObjects;
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
            "was also not registered by a @Bean method or manually");
      }

      args[i] = container.get(params[i].getType());
    }
    return args;
  }

  private static List<Method> getBeanMethods(Class<?> clazz) {
    return filterByAnnotation(List.of(clazz.getDeclaredMethods()), Bean.class);
  }

  private static List<Class<?>> getComponentClasses(Collection<Class<?>> classes) {
    return filterByAnnotation(classes, Component.class);
  }

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
