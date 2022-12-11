package simulation;

import gui.SimulationCanvas;

public class GameLoop {

  private static final int UPDATE_MILLIS = 50;

  private final SimulationCanvas canvas;

  public GameLoop(SimulationCanvas canvas) {
    this.canvas = canvas;
  }

  public void start() {
    new Thread(() -> {
      while (true) {
        canvas.repaint();
        try {
          Thread.sleep(UPDATE_MILLIS);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }).start();
  }
}
