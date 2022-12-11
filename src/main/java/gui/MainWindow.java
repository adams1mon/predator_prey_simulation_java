package gui;

import simulation.Field;
import simulation.GameLoop;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

  private final String title;

  private final SimulationCanvas canvas;
  private final GameLoop gameLoop;

  public MainWindow(String title, int fieldWidth, int fieldHeight, int rabbitCount, int foxCount) {
    this.title = title;
    this.canvas = new SimulationCanvas(fieldWidth, fieldHeight, rabbitCount, foxCount);
    this.gameLoop = new GameLoop(canvas);

    setupFrame();
    startGameLoop();
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
    var constraints = new GridBagConstraints();
    constraints.gridx = 0;
    constraints.gridy = 0;
    add(canvas, constraints);
  }

  private void addControlPanel() {
    var controlPanel = new ControlPanel(canvas.getField());
    var constraints = new GridBagConstraints();
    constraints.fill = GridBagConstraints.HORIZONTAL;
    constraints.gridx = 0;
    constraints.gridy = 1;
    add(controlPanel, constraints);
  }

  private void addStatisticsPanel() {
    var statsPanel = new StatisticsPanel(canvas.getField());
    var constraints = new GridBagConstraints();
    constraints.fill = GridBagConstraints.HORIZONTAL;
    constraints.gridx = 0;
    constraints.gridy = 2;
    add(statsPanel, constraints);
  }

  private void startGameLoop() {
    gameLoop.start();
  }
}
