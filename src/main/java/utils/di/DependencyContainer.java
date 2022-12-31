package utils.di;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Pair;
import utils.di.annotations.Autowired;
import utils.di.annotations.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
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
    resolveDependencies(container);
  }

  private static HashMap<Class<?>, Object> initializeClasses() {
    log.info("initializing classes");
    var container = new HashMap<Class<?>, Object>();
    getComponentClasses(PackageScanner.getClasses())
        .forEach(clazz -> {
          if (!container.containsKey(clazz)) {
            try {
              var constructor = clazz.getConstructor();
              container.put(clazz, constructor.newInstance());
            } catch (NoSuchMethodException e) {
              log.error(
                  "class {} doesn't have a public default constructor, skipping initialization: ",
                  clazz.getName()
              );
              throw new RuntimeException(clazz.getName() + " doesn't have a default public constructor");
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

  private static void resolveDependencies(HashMap<Class<?>, Object> objects) {
    log.info("resolving dependencies (class fields)");
    getAutowiredClassFields(objects.values())
        .forEach(pair -> {
          var field = pair.getFirst();
          var instance = pair.getSecond();
          var clazz = instance.getClass();
            if (objects.containsKey(field.getType())) {
              field.setAccessible(true);
              try {
                log.info("setting field {} of class {}", field.getName(), clazz.getName());

                field.set(instance, objects.get(field.getType()));

              } catch (IllegalAccessException e) {
                log.error("error while trying to set field {} of class {}", field.getName(), clazz.getName());
                e.printStackTrace();
              }
            } else {
              log.info("autowired field {} {} of class {} not found", field.getType(), field.getName(), clazz.getName());
            }
          });
  }

  private static Collection<Pair<Field, Object>> getAutowiredClassFields(Collection<Object> instances) {
    var fields = new LinkedList<Pair<Field, Object>>();
    instances.forEach(instance ->
        filterByAnnotation(
            List.of(instance.getClass().getDeclaredFields()),
            Autowired.class
        )
          .forEach(field -> fields.add(new Pair<>(field, instance)))
    );
    return fields;
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
