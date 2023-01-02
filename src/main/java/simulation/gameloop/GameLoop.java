package simulation.gameloop;

import gui.SimulationCanvas;
import config.Config;
import di.annotations.Autowired;
import di.annotations.Component;

@Component
public class GameLoop {

  private final int intervalMillis;

  private final SimulationCanvas canvas;
  private boolean running = false;
  private Thread thread;

  @Autowired
  public GameLoop(Config config, SimulationCanvas canvas) {
//    this.canvas = (SimulationCanvas) DependencyContainer.getInstance(SimulationCanvas.class);
//    var config = (Config) DependencyContainer.getInstance(Config.class);
    intervalMillis = config.getGameLoopInterval();
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
