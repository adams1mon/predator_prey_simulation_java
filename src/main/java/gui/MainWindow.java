package gui;

import simulation.gameloop.GameLoop;
import utils.LazyContainer;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

  private final String title;

  public MainWindow(String title) {
    this.title = title;
    setupFrame();
    initGameLoop();
  }

  private void setupFrame() {
    setTitle(title);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    setLayout(new GridBagLayout());

    addCanvas();
    addControlPanel();
    addStatisticsPanel();

    pack();
    setVisible(true);
  }

  private void addCanvas() {
    var canvas = (SimulationCanvas) LazyContainer.getInstance(SimulationCanvas.class);
    var constraints = new GridBagConstraints();
    constraints.gridx = 0;
    constraints.gridy = 0;
    add(canvas, constraints);
  }

  private void addControlPanel() {
    var controlPanel = (ControlPanel) LazyContainer.getInstance(ControlPanel.class);
    var constraints = new GridBagConstraints();
    constraints.fill = GridBagConstraints.HORIZONTAL;
    constraints.gridx = 0;
    constraints.gridy = 1;
    add(controlPanel, constraints);
  }

  private void addStatisticsPanel() {
    var statsPanel = (StatisticsPanel) LazyContainer.getInstance(StatisticsPanel.class);
    var constraints = new GridBagConstraints();
    constraints.fill = GridBagConstraints.HORIZONTAL;
    constraints.gridx = 0;
    constraints.gridy = 2;
    add(statsPanel, constraints);
  }

  private void initGameLoop() {
    var gameLoop = (GameLoop) LazyContainer.getInstance(GameLoop.class);
    gameLoop.repaintOnce();
  }
}
