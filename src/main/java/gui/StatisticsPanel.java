package gui;

import simulation.field.Field;
import stats.Statistics;
import utils.LazyContainer;

import javax.swing.*;
import java.awt.*;

public class StatisticsPanel extends JPanel {

  private final Statistics statistics;

  private final JLabel populationCountLabel = new JLabel();
  private final JLabel foxCountLabel = new JLabel();
  private final JLabel rabbitCountLabel = new JLabel();
  private final JLabel foxEnergyLabel = new JLabel();

  public StatisticsPanel() {
    this.statistics = new Statistics((Field) LazyContainer.getInstance(Field.class));

    updateLabels();
    statistics.addChangeListener(this::updateLabels);

    setLayout(new GridBagLayout());
    addLabels();
    addButtons();
  }

  private void addLabels() {
    var constraints = new GridBagConstraints();
    constraints.fill = GridBagConstraints.HORIZONTAL;
    constraints.gridx = 0;
    constraints.gridy = 0;
    add(populationCountLabel, constraints);

    constraints.gridy = 1;
    add(rabbitCountLabel, constraints);

    constraints.gridx = 1;
    add(foxCountLabel, constraints);

    constraints.gridx = 2;
    add(foxEnergyLabel, constraints);
  }

  private void addButtons() {
    var constraints = new GridBagConstraints();
    constraints.fill = GridBagConstraints.HORIZONTAL;

    var oneTimeStatsBtn = new JButton("Get current stats");
    oneTimeStatsBtn.addActionListener(e -> statistics.computeStats());

    constraints.gridy = 3;
    add(oneTimeStatsBtn, constraints);

    var startStatsBtn = new JButton("Start rolling stats");
    var stopStatsBtn = new JButton("Stop rolling stats");
    stopStatsBtn.setEnabled(false);

    startStatsBtn.addActionListener(e -> {
      statistics.startRollingStats();
      startStatsBtn.setEnabled(false);
      stopStatsBtn.setEnabled(true);
    });

    stopStatsBtn.addActionListener(e -> {
      statistics.stopRollingStats();
      startStatsBtn.setEnabled(true);
      stopStatsBtn.setEnabled(false);
    });

    constraints.gridx = 1;
    add(startStatsBtn, constraints);

    constraints.gridx = 2;
    add(stopStatsBtn, constraints);
  }

  private void updateLabels() {
    String populationCountTemplate = "Population count: %d";
    populationCountLabel.setText(String.format(populationCountTemplate, statistics.populationCount));

    String foxCountTemplate = "Fox count: %d (%.2f)";
    foxCountLabel.setText(String.format(foxCountTemplate, statistics.foxCount, statistics.foxPercentage));

    String rabbitCountTemplate = "Rabbit count: %d (%.2f)";
    rabbitCountLabel.setText(String.format(rabbitCountTemplate, statistics.rabbitCount, statistics.rabbitPercentage));

    String foxEnergyTemplate = "Fox avg energy: %.2f";
    foxEnergyLabel.setText(String.format(foxEnergyTemplate, statistics.foxAvgEnergy));
  }
}
