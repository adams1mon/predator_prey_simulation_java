package gui;

import di.annotations.Autowired;
import di.annotations.Component;
import simulation.field.Field;
import simulation.gameloop.GameLoop;

import javax.swing.*;
import java.awt.*;

@Component
public class ControlPanel extends JPanel {

  private int animalSpinnerValue = 20;
  private double foodSpinnerValue = 0.1;

  @Autowired
  public ControlPanel(Field field, GameLoop gameLoop) {

    setLayout(new FlowLayout());

    var animalSpinner = new JSpinner();
    animalSpinner.setModel(new SpinnerNumberModel(animalSpinnerValue, 1, 80, 1));
    animalSpinner.addChangeListener(e -> animalSpinnerValue = Integer.parseInt(animalSpinner.getValue().toString()));
    add(animalSpinner);

    var addRabbitsBtn = new JButton("Add rabbits");
    addRabbitsBtn.addActionListener(e -> field.addRabbits(animalSpinnerValue));
    add(addRabbitsBtn);

    var addFoxesBtn = new JButton("Add foxes");
    addFoxesBtn.addActionListener(e -> field.addFoxes(animalSpinnerValue));
    add(addFoxesBtn);

    var foodSpinner = new JSpinner();
    foodSpinner.setModel(new SpinnerNumberModel(foodSpinnerValue, 0.01, 0.99, 0.001));
    foodSpinner.addChangeListener(e -> foodSpinnerValue = Double.parseDouble(foodSpinner.getValue().toString()));
    add(foodSpinner);

    var changeFoodSpawnChanceBtn = new JButton("Set food spawn chance");
    changeFoodSpawnChanceBtn.addActionListener(e -> field.setFoodSpawnChance(foodSpinnerValue));
    add(changeFoodSpawnChanceBtn);

    var toggleStopStartBtn = new JButton(gameLoop.isRunning() ? "Stop" : "Start");
    toggleStopStartBtn.addActionListener(e -> {
      if (gameLoop.isRunning()) {
        gameLoop.stop();
        toggleStopStartBtn.setText("Start");
      } else {
        gameLoop.start();
        toggleStopStartBtn.setText("Stop");
      }
    });
    add(toggleStopStartBtn);

    var clearBtn = new JButton("Clear");
    clearBtn.addActionListener(e -> {
      gameLoop.stop();
      toggleStopStartBtn.setText("Start");
      field.clear();
      gameLoop.repaintOnce();
    });
    add(clearBtn);
  }
}
