package simulation.field;

import config.Config;
import config.ConfigValue;
import di.annotations.Autowired;
import di.annotations.Component;
import simulation.entities.FieldEntity;
import simulation.entities.Food;
import simulation.entities.Fox;
import simulation.entities.Rabbit;

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

  private final int width;
  private final int height;
  private double foodSpawnChance;

  private final ArrayList<ArrayList<FieldEntity>> grid = new ArrayList<>();
  private final HashSet<FieldEntity> entities = new HashSet<>();

  private final ReadWriteLock gridLock = new ReentrantReadWriteLock();
  private final Lock readLock = gridLock.readLock();
  private final Lock writeLock = gridLock.writeLock();

  private final Random random = new Random();

  @Autowired
  public Field() {
    width = Config.getIntProperty(ConfigValue.WIDTH);
    height = Config.getIntProperty(ConfigValue.HEIGHT);
    foodSpawnChance = Config.getDoubleProperty(ConfigValue.FOOD_SPAWN_CHANCE);

    createEmptyGrid();
    addRabbits(Config.getIntProperty(ConfigValue.RABBIT_COUNT));
    addFoxes(Config.getIntProperty(ConfigValue.FOX_COUNT));
  }

  public Collection<FieldEntity> getEntities() {
    try {
      readLock.lock();
      return new HashSet<>(entities);
    } finally {
      readLock.unlock();
    }
  }

  public void addRabbits(int count) {
    for (int i = 0; i < count; ++i) {
      tryToPlaceOnFreeCell(new Rabbit(this));
    }
  }

  public void addFoxes(int count) {
    for (int i = 0; i < count; ++i) {
      tryToPlaceOnFreeCell(new Fox(this));
    }
  }

  public void setFoodSpawnChance(double chance) {
    foodSpawnChance = chance;
  }

  private void createEmptyGrid() {
    for (int i = 0; i < height; ++i) {
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
  public void add(int x, int y, FieldEntity entity) {
    try {
      writeLock.lock();
      entity.setPosition(x, y);
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
  public void simulate() {
    spawnFood();

    readLock.lock();
    var entitiesCopy = new HashSet<>(entities);
    readLock.unlock();

    for (FieldEntity entity : entitiesCopy) {

      // it can happen that the original "animals" set doesn't contain the current entity any more (overridden by move)
      readLock.lock();
      var outdated = !entities.contains(entity);
      readLock.unlock();

      if (!outdated) {
        entity.move();
        entity.spawnOffspring();
        entity.loseEnergy();
      }
    }
  }

  private void spawnFood() {
    writeLock.lock();
    for (int i = 0; i < height; ++i) {
      for (int j = 0; j < width; ++j) {
        if (grid.get(i).get(j) == null && random.nextDouble() < foodSpawnChance) {
          add(j, i, new Food(this));
        }
      }
    }
    writeLock.unlock();
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
