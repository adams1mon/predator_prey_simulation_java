import gui.MainWindow;
import gui.SimulationCanvas;
import simulation.GameLoop;
import utils.LazyContainer;

public class Main {
  public static void main(String[] args) {

    int width = 120;
    int height = 80;
    int rabbits = 50;
    int foxes = 50;

//    LazyContainer.register(new SimulationCanvas(width, height, rabbits, foxes));
//    LazyContainer.register(new GameLoop((SimulationCanvas) LazyContainer.getInstance(SimulationCanvas.class)));

    new MainWindow(
        "Something",
        width,
        height,
        rabbits,
        foxes
    );
  }
}
