import gui.ControlPanel;
import gui.MainWindow;
import gui.SimulationCanvas;
import gui.StatisticsPanel;
import simulation.Field;
import simulation.GameLoop;
import utils.LazyContainer;

public class Main {
  public static void main(String[] args) {

    int width = 120;
    int height = 80;
    int rabbits = 50;
    int foxes = 50;

    LazyContainer.register(new Field(width, height, rabbits, foxes));
    LazyContainer.register(new SimulationCanvas());
    LazyContainer.register(new GameLoop());
    LazyContainer.register(new ControlPanel());
    LazyContainer.register(new StatisticsPanel());

    new MainWindow("Something");
  }
}
