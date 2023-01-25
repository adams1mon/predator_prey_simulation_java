package simulation.gameloop;

import config.Config;
import config.ConfigValue;
import di.annotations.Autowired;
import di.annotations.Component;
import gui.SimulationCanvas;
import simulation.field.Field;

@Component
public class GameLoop {

  private final int updateIntervalMillis;
  private final int updateIntervalLimitMillis;

  private final SimulationCanvas canvas;
  private final Field field;
  private boolean running = false;
  private Thread thread;

  @Autowired
  public GameLoop(SimulationCanvas canvas) {
    updateIntervalMillis = Config.getIntProperty(ConfigValue.UPDATE_INTERVAL_MILLIS);
    updateIntervalLimitMillis = Config.getIntProperty(ConfigValue.UPDATE_INTERVAL_LIMIT_MILLIS);
    this.canvas = canvas;
    field = canvas.getField();
  }

  public void start() {
    if (running) return;

    thread = new Thread(() -> {
      running = true;

      double previous = System.currentTimeMillis();
      double lag = 0;
      while (running) {

        double current = System.currentTimeMillis();
        double elapsed = current - previous;
        previous = current;
        lag += elapsed;

        while (lag >= updateIntervalMillis) {
          field.simulate();
          lag -= updateIntervalMillis;
        }

        canvas.repaint();
        try {
          Thread.sleep(updateIntervalLimitMillis);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    });

    thread.start();
  }

  public void stop() {
    if (!running) return;
    try {
      running = false;
      thread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public boolean isRunning() {
    return running;
  }

  public void repaintOnce() {
    canvas.repaint();
  }
}
