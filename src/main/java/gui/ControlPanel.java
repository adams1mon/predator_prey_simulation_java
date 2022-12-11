package gui;

import simulation.Field;

import javax.swing.*;
import java.awt.*;

public class ControlPanel extends JPanel {

  private int spinnerValue = 20;

  public ControlPanel(Field field) {

    setLayout(new FlowLayout());

    var spinner = new JSpinner();
    spinner.setModel(new SpinnerNumberModel(spinnerValue, 1, 80, 1));
    spinner.addChangeListener((event) -> ++spinnerValue);

    add(spinner);

    var addRabbitsBtn = new JButton("Add rabbits");
    addRabbitsBtn.addActionListener(e -> field.addRabbits(spinnerValue));
    add(addRabbitsBtn);

    var addFoxesBtn = new JButton("Add foxes");
    addFoxesBtn.addActionListener(e -> field.addFoxes(spinnerValue));
    add(addFoxesBtn);

    var cloneAll = new JButton("Clone all");
    cloneAll.addActionListener(e -> field.cloneAll());
    add(cloneAll);

    var clearBtn = new JButton("Clear");
    clearBtn.addActionListener(e -> field.clear());
    add(clearBtn);
  }
}
