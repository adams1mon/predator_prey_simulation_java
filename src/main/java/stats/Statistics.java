package stats;

import simulation.field.Field;
import simulation.entities.Animal;
import simulation.entities.Fox;
import simulation.entities.Rabbit;

import java.util.LinkedList;

public class Statistics {

  public int foxCount = 0;
  public int rabbitCount = 0;
  public int populationCount = 0;

  public double foxPercentage = 0.0;
  public double rabbitPercentage = 0.0;

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
    var foxAllEnergy = 0.0;

    for (Animal animal : field.getAnimals()) {
      if (animal instanceof Rabbit) {
        ++newRabbitCount;
      } else if (animal instanceof Fox) {
        ++newFoxCount;
        foxAllEnergy += animal.getEnergy();
      }
    }

    foxCount = newFoxCount;
    rabbitCount = newRabbitCount;
    populationCount = foxCount + rabbitCount;

    foxPercentage = foxCount / (double) populationCount;
    rabbitPercentage = rabbitCount / (double) populationCount;

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
