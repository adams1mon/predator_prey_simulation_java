package simulation;

import simulation.entities.Animal;
import simulation.entities.Fox;
import simulation.entities.Rabbit;

import java.awt.*;
import java.util.*;

public class Field {

  private final int width;
  private final int height;

  private final ArrayList<ArrayList<Animal>> grid = new ArrayList<>();
  private HashSet<Animal> animals = new HashSet<>();
  private LinkedList<Animal> addBuffer = new LinkedList<>();
  private LinkedList<Animal> removeBuffer = new LinkedList<>();

  public Field(int width, int height, int rabbitCount, int foxCount) {
    this.width = width;
    this.height = height;

    for (int i = 0; i < height; ++i) {
      var row = new ArrayList<Animal>();
      for (int j = 0; j < width; ++j) {
        row.add(null);
      }
      grid.add(row);
    }

    addRabbits(rabbitCount);
    addFoxes(foxCount);
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public Collection<Animal> getAnimals() {
    return animals;
  }

  public void addRabbits(int count) {
    var firstRabbit = new Rabbit();
    tryToPlaceOnFreeCell(firstRabbit);

    for (int i = 0; i < count; ++i) {
      tryToPlaceOnFreeCell(firstRabbit.clone());
    }
  }

  public void addFoxes(int count) {
    var firstFox = new Fox();
    tryToPlaceOnFreeCell(firstFox);

    for (int i = 0; i < count; ++i) {
      tryToPlaceOnFreeCell(firstFox.clone());
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

  public boolean cellTaken(int x, int y) {
    return grid.get(y).get(x) != null;
  }

  public boolean rabbitOnCell(int x, int y) {
    return grid.get(y).get(x) instanceof Rabbit;
  }

  public void add(int x, int y, Animal animal) {
    addBuffer.add(animal);
    grid.get(y).set(x, animal);
  }

  public void remove(int x, int y) {
    removeBuffer.add(grid.get(y).get(x));
    grid.get(y).set(x, null);
  }

  public void move(int oldX, int oldY, int newX, int newY) {
    grid.get(newY).set(newX, grid.get(oldY).get(oldX));
    grid.get(oldY).set(oldX, null);
  }

  public void draw(Graphics graphics) {
    for (Animal animal : animals) {
      animal.draw(graphics);
    }
  }

  public void simulate() {
    updateAnimals();

    for (Animal animal : animals) {
      animal.move(this);
      animal.spawnOffspring(this);
      animal.loseEnergy(this);
    }
  }

  /**
   * There is a concurrency issue here that i am not aware of...
   * Without copying the array a second time (newAnimalsCopy),
   * foxes with negative don't get deleted properly from the 'animals' array
   * in spite of the remove buffer
   */
  private void updateAnimals() {
    var newAnimals = new HashSet<>(animals);
    newAnimals.addAll(addBuffer);
    removeBuffer.forEach(newAnimals::remove);

    addBuffer = new LinkedList<>();
    removeBuffer = new LinkedList<>();

//        animals = newAnimals

    var newAnimalsCopy = new HashSet<>(newAnimals);

    for (Animal animal : newAnimals) {
      if (animal instanceof Fox) {
        if (animal.getEnergy() <= 0) {
//                    this gets run a lof of times...
//                    println("update: removing fox energy: ${animal.energy}")
          newAnimalsCopy.remove(animal);
//                    val (x, y) = animal.getPosition()
//                    grid[y][x] = null
        }
      }
    }

//        animals = newAnimals
    animals = newAnimalsCopy;
  }

  public void clear() {
    animals = new HashSet<>();
    for (int i = 0; i < height; ++i) {
      for (int j = 0; j < width; ++j) {
        grid.get(i).set(j, null);
      }
    }
    addBuffer = new LinkedList<>();
    removeBuffer = new LinkedList<>();
  }

  public void cloneAll() {
    for (Animal animal : animals) {
      tryToPlaceOnFreeCell(animal);
    }
  }
}
