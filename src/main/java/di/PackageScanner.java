package di;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class PackageScanner {

  private static final String PACKAGE_ROOT = "";
  private static final Logger log = LoggerFactory.getLogger(PackageScanner.class);
  private static final List<File> resourceDirectories = new LinkedList<>();
  private static final Set<Class<?>> classes = new HashSet<>();

  static {
    try {
      // find all classes, beginning from the package root
      resourceDirectories.addAll(findResourceDirectories(PACKAGE_ROOT));
      classes.addAll(findClassesInDirectories(resourceDirectories, PACKAGE_ROOT));
    } catch (Exception e) {
      log.error("error while scanning resource directories and classes");
      e.printStackTrace();
      System.exit(1);
    }
  }

  private static List<File> findResourceDirectories(String packageName) throws IOException {
    String path = packageName.replace('.', '/');
    log.info("scanning resource path '{}'", path);

    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    if (classLoader == null) {
      log.error("classLoader null, exiting");
      System.exit(1);
    }

    // create a list of directories (files) from the classpath resource URLs
    var resources = classLoader.getResources(path);

    var dirs = new ArrayList<File>();
    while (resources.hasMoreElements()) {
      URL resource = resources.nextElement();
      dirs.add(new File(resource.getFile()));
      log.info("found resource {}", resource);
    }
    return dirs;
  }

  private static Set<Class<?>> findClassesInDirectories(List<File> directories, String packageName)
      throws ClassNotFoundException {
    var classNames = new HashSet<String>();
    directories.forEach(directory -> classNames.addAll(findClassesRecursive(directory, packageName)));

    // add reflection class instances to a set
    var classes = new HashSet<Class<?>>();
    for (String className : classNames) {
      log.info("getting reflection instance of {}", className);
      classes.add(Class.forName(className));
    }
    return classes;
  }

  private static Set<String> findClassesRecursive(File directory, String packageName) {
    log.info("looking recursively in resource path '{}'", directory.getAbsolutePath());
    var classes = new HashSet<String>();

    if (directory.getAbsolutePath().contains("!") && directory.getAbsolutePath().split("!")[0].endsWith(".jar")) {
      log.info("found jar file, continuing...");
      return classes;
    }

    if (!directory.exists()) {
      return classes;
    }

    var files = directory.listFiles();
    if (files == null) {
      return classes;
    }

    String packagePrefix = packageName.isBlank() ? "" : packageName + ".";
    for (File file : files) {
      if (file.isDirectory()) {
        classes.addAll(findClassesRecursive(file, packagePrefix + file.getName()));
      } else if (file.getName().endsWith(".class")) {
        log.info("found class {}", file.getName());
        classes.add(packagePrefix + file.getName().substring(0, file.getName().length() - 6));
      }
    }

    return classes;
  }

  public static Set<Class<?>> getClasses() {
    return classes;
  }
}
