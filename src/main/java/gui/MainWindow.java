package gui;

import di.annotations.Autowired;
import di.annotations.Component;
import simulation.gameloop.GameLoop;

import javax.swing.*;
import java.awt.*;

@Component
public class MainWindow extends JFrame {

  @Autowired
  public MainWindow(
      SimulationCanvas canvas,
      ControlPanel controlPanel,
      StatisticsPanel statisticsPanel,
      GameLoop gameLoop
  ) {
    setupFrame("default title", canvas, controlPanel, statisticsPanel);
    initGameLoop(gameLoop);
  }

  private void setupFrame(
      String title,
      SimulationCanvas canvas,
      ControlPanel controlPanel,
      StatisticsPanel statisticsPanel
  ) {
    setTitle(title);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    setLayout(new GridBagLayout());

    addCanvas(canvas);
    addControlPanel(controlPanel);
    addStatisticsPanel(statisticsPanel);

    pack();
    setVisible(true);
  }

  private void addCanvas(SimulationCanvas canvas) {
//    var canvas = (SimulationCanvas) DependencyContainer.getInstance(SimulationCanvas.class);
    var constraints = new GridBagConstraints();
    constraints.gridx = 0;
    constraints.gridy = 0;
    add(canvas, constraints);
  }

  private void addControlPanel(ControlPanel controlPanel) {
//    var controlPanel = (ControlPanel) DependencyContainer.getInstance(ControlPanel.class);
    var constraints = new GridBagConstraints();
    constraints.fill = GridBagConstraints.HORIZONTAL;
    constraints.gridx = 0;
    constraints.gridy = 1;
    add(controlPanel, constraints);
  }

  private void addStatisticsPanel(StatisticsPanel statisticsPanel) {
//    var statisticsPanel = (StatisticsPanel) DependencyContainer.getInstance(StatisticsPanel.class);
    var constraints = new GridBagConstraints();
    constraints.fill = GridBagConstraints.HORIZONTAL;
    constraints.gridx = 0;
    constraints.gridy = 2;
    add(statisticsPanel, constraints);
  }

  private void initGameLoop(GameLoop gameLoop) {
//    var gameLoop = (GameLoop) DependencyContainer.getInstance(GameLoop.class);
    gameLoop.repaintOnce();
  }
}
