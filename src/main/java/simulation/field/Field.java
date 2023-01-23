package simulation.field;

import config.Config;
import di.annotations.Autowired;
import di.annotations.Component;
import simulation.entities.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Component
public class Field {

//  public static final int FOOD_PER_ROUND = 50;
//  public static final double FOOD_SPAWN_CHANCE = 0.1;

  private double foodSpawnChance = 0.1;

  private final int width;
  private final int height;

//  private final ArrayList<ArrayList<Animal>> grid = new ArrayList<>();
  private final ArrayList<ArrayList<FieldEntity>> grid = new ArrayList<>();
//  private final HashSet<Animal> animals = new HashSet<>();
  private final HashSet<FieldEntity> entities = new HashSet<>();

  private final ReadWriteLock gridLock = new ReentrantReadWriteLock();
  private final Lock readLock = gridLock.readLock();
  private final Lock writeLock = gridLock.writeLock();

  private final Random random = new Random();

  @Autowired
  public Field(Config config) {

    this.width = config.getWidth();
    this.height = config.getHeight();

    createEmptyGrid();
    addRabbits(config.getRabbitCount());
    addFoxes(config.getFoxCount());
  }

//  public Collection<Animal> getAnimals() {
//    try {
//      readLock.lock();
//      return new HashSet<>(entities);
//    } finally {
//      readLock.unlock();
//    }
//  }

  public Collection<FieldEntity> getEntities() {
    try {
      readLock.lock();
      return new HashSet<>(entities);
    } finally {
      readLock.unlock();
    }
  }

  public void addFood(int count) {
    for (int i = 0; i < count; ++i) {
      tryToPlaceOnFreeCell(new Food());
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

  public void setFoodSpawnChance(double chance) {
    foodSpawnChance = chance;
  }

  private void createEmptyGrid() {
    for (int i = 0; i < height; ++i) {
//      var row = new ArrayList<Animal>();
      var row = new ArrayList<FieldEntity>();
      for (int j = 0; j < width; ++j) {
        row.add(null);
      }
      grid.add(row);
    }
  }

  private void tryToPlaceOnFreeCell(FieldEntity entity) {
    var iterLimit = 50;
    var i = 0;
    int x, y;
    do {
      ++i;
      x = random.nextInt(width);
      y = random.nextInt(height);
    } while (cellTaken(x, y) && i < iterLimit);

    if (i >= iterLimit) {
      System.out.println("placeOnField() iteration limit " + iterLimit + " reached");
      return;
    }

    entity.setPosition(x, y);
    add(x, y, entity);
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
  public FieldEntity getEntity(int x, int y) {
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
//  public void add(int x, int y, Animal animal) {
//    try {
//      writeLock.lock();
//      animals.add(animal);
//      grid.get(y).set(x, animal);
//    } finally {
//      writeLock.unlock();
//    }
//  }

  public void add(int x, int y, FieldEntity entity) {
    try {
      writeLock.lock();
      entities.add(entity);
      grid.get(y).set(x, entity);
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
      entities.remove(grid.get(y).get(x));
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
//  public void draw(Graphics graphics) {
//    try {
//      readLock.lock();
//      for (Animal animal : entities) {
//        animal.draw(graphics);
//      }
//    } finally {
//      readLock.unlock();
//    }
//  }

  public void draw(Graphics graphics) {
    try {
      readLock.lock();
      for (FieldEntity entity : entities) {
        entity.draw(graphics);
      }
    } finally {
      readLock.unlock();
    }
  }

  /**
   * "animals & grid" mutated from UI through addFoxes/addRabbits --> synchronization needed
   */
//  public void simulate() {
//
//    spawnFood();
//
//    readLock.lock();
//    var animalsCopy = new HashSet<>(entities);
//    readLock.unlock();
//
//    for (Animal animal : animalsCopy) {
//
//      // it can happen that the original "animals" set doesn't contain the current entity any more (overridden by move)
//      readLock.lock();
//      var outdated = !entities.contains(animal);
//      readLock.unlock();
//
//      if (!outdated) {
//       animal.move(this);
//       animal.spawnOffspring(this);
//       animal.loseEnergy(this);
//      }
//    }
//  }

  public void simulate() {

//    addFood(FOOD_PER_ROUND);

//    readLock.lock();
//    for (int i = 0; i < height; ++i) {
//      for (int j = 0; j < width; ++j) {
//        if (!cellTaken(j, i) && random.nextDouble() < FOOD_SPAWN_CHANCE) {
//          add(j, i, new Food());
//        }
//      }
//    }
//    readLock.unlock();

    writeLock.lock();
    for (int i = 0; i < height; ++i) {
      for (int j = 0; j < width; ++j) {
        if (grid.get(i).get(j) == null && random.nextDouble() < foodSpawnChance) {
          var food = new Food();
          entities.add(food);
          grid.get(i).set(j, food);
        }
      }
    }
    writeLock.unlock();

    readLock.lock();
    var entitiesCopy = new HashSet<>(entities);
    readLock.unlock();

    for (FieldEntity entity : entitiesCopy) {

      // it can happen that the original "animals" set doesn't contain the current entity any more (overridden by move)
      readLock.lock();
      var outdated = !entities.contains(entity);
      readLock.unlock();

      if (!outdated) {
        entity.move(this);
        entity.spawnOffspring(this);
        entity.loseEnergy(this);
      }
    }
  }

  /**
   * "grid" mutated from UI through addFoxes/addRabbits --> synchronization needed
   */
  public void clear() {
    try {
      writeLock.lock();
      entities.clear();
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
