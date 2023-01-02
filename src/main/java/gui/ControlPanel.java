package gui;

import di.annotations.Autowired;
import di.annotations.Component;
import simulation.field.Field;
import simulation.gameloop.GameLoop;

import javax.swing.*;
import java.awt.*;

@Component
public class ControlPanel extends JPanel {

  private int spinnerValue = 20;

  @Autowired
  public ControlPanel(Field field, GameLoop gameLoop) {

//    var field = (Field) DependencyContainer.getInstance(Field.class);
//    var gameLoop = (GameLoop) DependencyContainer.getInstance(GameLoop.class);

    setLayout(new FlowLayout());

    var spinner = new JSpinner();
    spinner.setModel(new SpinnerNumberModel(spinnerValue, 1, 80, 1));
    spinner.addChangeListener((event) -> spinnerValue = Integer.parseInt(spinner.getValue().toString()));
    add(spinner);

    var addRabbitsBtn = new JButton("Add rabbits");
    addRabbitsBtn.addActionListener(e -> field.addRabbits(spinnerValue));
    add(addRabbitsBtn);

    var addFoxesBtn = new JButton("Add foxes");
    addFoxesBtn.addActionListener(e -> field.addFoxes(spinnerValue));
    add(addFoxesBtn);

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
