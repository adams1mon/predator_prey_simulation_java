package simulation.gameloop;

import gui.SimulationCanvas;
import config.Config;
import utils.di.DependencyContainer;

public class GameLoop {

  private final int intervalMillis;

  private final SimulationCanvas canvas;
  private boolean running = false;
  private Thread thread;

  public GameLoop() {
    this.canvas = (SimulationCanvas) DependencyContainer.getInstance(SimulationCanvas.class);
    var config = (Config) DependencyContainer.getInstance(Config.class);
    intervalMillis = config.getGameLoopInterval();
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
