package stats;

import simulation.entities.FieldEntity;
import simulation.entities.Food;
import simulation.entities.Fox;
import simulation.entities.Rabbit;
import simulation.field.Field;

import java.util.LinkedList;

public class Statistics {

  public int foxCount = 0;
  public int rabbitCount = 0;
  public int foodCount = 0;
  public int populationCount = 0;

  public double foxPercentage = 0.0;
  public double rabbitPercentage = 0.0;
  public double foodPercentage = 0.0;

  public double foxAvgEnergy = 0.0;

  private final LinkedList<Runnable> changeListeners = new LinkedList<>();

  private final Field field;
  private Thread statisticsThread = null;
  private boolean statisticsThreadRunning = false;

  public Statistics(Field field) {
    this.field = field;
  }

  public void computeStats() {
    computeStatsForField();
    runChangeListeners();
  }

  private void computeStatsForField() {
    var newFoxCount = 0;
    var newRabbitCount = 0;
    var newFoodCount = 0;
    var foxAllEnergy = 0.0;

    for (FieldEntity entity : field.getEntities()) {
      if (entity instanceof Rabbit) {
        ++newRabbitCount;
      } else if (entity instanceof Fox) {
        ++newFoxCount;
        foxAllEnergy += ((Fox) entity).getEnergy();
      } else if (entity instanceof Food) {
        ++newFoodCount;
      }
    }

    foxCount = newFoxCount;
    rabbitCount = newRabbitCount;
    foodCount = newFoodCount;
    populationCount = foxCount + rabbitCount;

    foxPercentage = foxCount / (double) populationCount;
    rabbitPercentage = rabbitCount / (double) populationCount;

    // food percentage according to the "living" population
    foodPercentage = foodCount / (double) populationCount;

    foxAvgEnergy = foxAllEnergy / (double) foxCount;
  }

  public void startRollingStats() {
    if (statisticsThreadRunning) return;

    statisticsThread = new Thread(() -> {
      statisticsThreadRunning = true;

      while(statisticsThreadRunning) {
        computeStats();
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    });

    statisticsThread.start();
  }

  public void stopRollingStats() {
    if (!statisticsThreadRunning) return;
    try {
      statisticsThreadRunning = false;
      statisticsThread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public void addChangeListener(Runnable runnable) {
    changeListeners.add(runnable);
  }

  public void runChangeListeners() {
    for (Runnable runnable : changeListeners) {
      runnable.run();
    }
  }
}
