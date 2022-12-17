package simulation;

import gui.SimulationCanvas;

public class GameLoop {

  private static final int UPDATE_MILLIS = 50;

  private final SimulationCanvas canvas;
  private boolean running = false;
  private Thread thread;

  public GameLoop(SimulationCanvas canvas) {
    this.canvas = canvas;
  }

  public void start() {
    if (running) return;

    thread = new Thread(() -> {
      running = true;
      while (running) {
        canvas.repaint();
        try {
          Thread.sleep(UPDATE_MILLIS);
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
