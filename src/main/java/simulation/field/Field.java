package simulation.field;

import config.Config;
import simulation.entities.Animal;
import simulation.entities.Fox;
import simulation.entities.Rabbit;
import utils.LazyContainer;

import java.awt.*;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Field {

  private final int width;
  private final int height;

  private final ArrayList<ArrayList<Animal>> grid = new ArrayList<>();
  private final HashSet<Animal> animals = new HashSet<>();

  private final ReadWriteLock gridLock = new ReentrantReadWriteLock();
  private final Lock readLock = gridLock.readLock();
  private final Lock writeLock = gridLock.writeLock();

  public Field() {
    var config = (Config) LazyContainer.getInstance(Config.class);

    this.width = config.getWidth();
    this.height = config.getHeight();

    createEmptyGrid();
    addRabbits(config.getRabbitCount());
    addFoxes(config.getFoxCount());
  }

  public Collection<Animal> getAnimals() {
    try {
      readLock.lock();
      return new HashSet<>(animals);
    } finally {
      readLock.unlock();
    }
  }

  public void addRabbits(int count) {
    for (int i = 0; i < count; ++i) {
      tryToPlaceOnFreeCell(new Rabbit());
    }
  }

  public void addFoxes(int count) {
    for (int i = 0; i < count; ++i) {
      tryToPlaceOnFreeCell(new Fox());
    }
  }

  private void createEmptyGrid() {
    for (int i = 0; i < height; ++i) {
      var row = new ArrayList<Animal>();
      for (int j = 0; j < width; ++j) {
        row.add(null);
      }
      grid.add(row);
    }
  }

  private void tryToPlaceOnFreeCell(Animal animal) {
    var iterLimit = 50;
    var i = 0;
    int x, y;
    var random = new Random();
    do {
      ++i;
      x = random.nextInt(width);
      y = random.nextInt(height);
    } while (cellTaken(x, y) && i < iterLimit);

    if (i >= iterLimit) {
      System.out.println("placeOnField() iteration limit " + iterLimit + " reached");
      return;
    }

    animal.setPosition(x, y);
    add(x, y, animal);
  }

  /**
   * "grid" mutated from UI through addFoxes/addRabbits --> synchronization needed
   */
  public boolean cellTaken(int x, int y) {
    try {
      readLock.lock();
      return grid.get(y).get(x) != null;
    } finally {
      readLock.unlock();
    }
  }

  /**
   * "grid" mutated from UI through addFoxes/addRabbits --> synchronization needed
   */
  public Animal getAnimal(int x, int y) {
    try {
      readLock.lock();
      return grid.get(y).get(x);
    } finally {
      readLock.unlock();
    }
  }

  /**
   * "animals & grid" mutated from UI through addFoxes/addRabbits --> synchronization needed
   */
  public void add(int x, int y, Animal animal) {
    try {
      writeLock.lock();
      animals.add(animal);
      grid.get(y).set(x, animal);
    } finally {
      writeLock.unlock();
    }
  }

  /**
   * "animals & grid" mutated from UI through addFoxes/addRabbits --> synchronization needed
   */
  public void remove(int x, int y) {
    try {
      writeLock.lock();
      animals.remove(grid.get(y).get(x));
      grid.get(y).set(x, null);
    } finally {
      writeLock.unlock();
    }
  }

  /**
   * "grid" mutated from UI through addFoxes/addRabbits --> synchronization needed
   */
  public void move(int oldX, int oldY, int newX, int newY) {
    try {
      writeLock.lock();
      grid.get(newY).set(newX, grid.get(oldY).get(oldX));
      grid.get(oldY).set(oldX, null);
    } finally {
      writeLock.unlock();
    }
  }

  /**
   * "animals & grid" mutated from UI through addFoxes/addRabbits --> synchronization needed
   */
  public void draw(Graphics graphics) {
    try {
      readLock.lock();
      for (Animal animal : animals) {
        animal.draw(graphics);
      }
    } finally {
      readLock.unlock();
    }
  }

  /**
   * "animals & grid" mutated from UI through addFoxes/addRabbits --> synchronization needed
   */
  public void simulate() {
    readLock.lock();
    var animalsCopy = new HashSet<>(animals);
    readLock.unlock();

    for (Animal animal : animalsCopy) {

      // it can happen that the original "animals" set doesn't contain the current entity anymore (overridden by move)
      readLock.lock();
      var outdated = !animals.contains(animal);
      readLock.unlock();

      if (!outdated) {
       animal.move(this);
       animal.spawnOffspring(this);
       animal.loseEnergy(this);
      }
    }
  }

  /**
   * "grid" mutated from UI through addFoxes/addRabbits --> synchronization needed
   */
  public void clear() {
    try {
      writeLock.lock();
      animals.clear();
      for (int i = 0; i < height; ++i) {
        for (int j = 0; j < width; ++j) {
          grid.get(i).set(j, null);
        }
      }
    } finally {
      writeLock.unlock();
    }
  }
}
