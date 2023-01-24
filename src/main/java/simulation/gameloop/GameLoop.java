package simulation.gameloop;

import config.Config;
import config.ConfigValue;
import di.annotations.Autowired;
import di.annotations.Component;
import gui.SimulationCanvas;

@Component
public class GameLoop {

  private final int intervalMillis;

  private final SimulationCanvas canvas;
  private boolean running = false;
  private Thread thread;

  @Autowired
  public GameLoop(SimulationCanvas canvas) {
    intervalMillis = Config.getIntProperty(ConfigValue.UPDATE_INTERVAL_MILLIS);
    this.canvas = canvas;
  }

  public void start() {
    if (running) return;

    thread = new Thread(() -> {
      running = true;
      while (running) {
        canvas.repaint();
        try {
          Thread.sleep(intervalMillis);
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
