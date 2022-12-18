import gui.ControlPanel;
import gui.MainWindow;
import gui.SimulationCanvas;
import gui.StatisticsPanel;
import simulation.field.Field;
import config.Config;
import simulation.gameloop.GameLoop;
import utils.LazyContainer;

public class Main {
  public static void main(String[] args) {

    LazyContainer.register(new Config());
    LazyContainer.register(new Field());
    LazyContainer.register(new SimulationCanvas());
    LazyContainer.register(new GameLoop());
    LazyContainer.register(new ControlPanel());
    LazyContainer.register(new StatisticsPanel());

    new MainWindow("Something");
  }
}
