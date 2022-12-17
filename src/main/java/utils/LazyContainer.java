package utils;

import java.util.HashMap;

public class LazyContainer {

  private static final HashMap<Class<?>, Object> objects = new HashMap<>();

  public static void register(Object obj) {
    if(!objects.containsKey(obj.getClass())) {
      objects.put(obj.getClass(), obj);
    }
  }

  public static Object getInstance(Class<?> clazz) {
    if (!objects.containsKey(clazz)) {
      throw new RuntimeException(clazz.getName() + " instance does not exist in the factory");
    }
    return objects.get(clazz);
  }
}
