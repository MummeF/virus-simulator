package Gui.Settings;

import process.ConfigLib;
import process.Simulation;
import process.time.TimeUnit;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.stream.Collectors;

import static process.ConfigLib.*;

public class SingleInputPanel extends JPanel {
    public static final String SIZE = "size-factor", MOBILITY = "mobility",
            POPULATION = "population", RISKAGE = "riskage", LETHALITY = "lethality",
            LETHALITYPLUSFORRISKAGE = "lethality-plus-for-risk-age", INCUBATION = "incubation",
            ILLNESS = "illness", INFECTIONONFIELD = "infection-on-field", TIMESPEED = "timespeed",
            TIMESPEEDSTEP = "timespeedstep", ANIMATION = "bool_animation", TIMEUNIT = "comb_timeunit", DIRECTION ="direction";


    private String handledValue;
    private JLabel description;

    public SingleInputPanel(int width, int height, String handledValue, String descriptionText) {
        this.handledValue = handledValue;
        this.setPreferredSize(new Dimension(width, height));
        description = new JLabel(descriptionText, SwingConstants.RIGHT);
        description.setFont(new Font(description.getFont().getName(), Font.PLAIN, description.getFont().getSize()));
        this.addComponent(description, width / 4 * 3 - 10, height);
        if (handledValue.contains("bool")) {
            JPanel boolPanel = new JPanel();
            boolPanel.setPreferredSize(new Dimension(width / 4 - 10, height));
            JCheckBox valueBox = new JCheckBox();
            switch (handledValue) {
                case ANIMATION:
                    valueBox.setSelected(ANIMATED_MOVE);
                    valueBox.addActionListener(e -> ANIMATED_MOVE = valueBox.isSelected());
                    break;
            }
            boolPanel.add(valueBox);
            this.add(boolPanel);
        } else if (handledValue.equals(TIMEUNIT)) {
            Object[] values = Arrays.stream(TimeUnit.values())
                    .map(timeUnit -> timeUnit.getLongName())
                    .collect(Collectors.toList()).toArray();
            JComboBox comboBox = new JComboBox(values);
            comboBox.setSelectedItem(TIME_UNIT.getLongName());
            comboBox.addActionListener(e ->
                    TIME_UNIT = TimeUnit.ofLongName((String) comboBox.getSelectedItem())
            );
            this.addComponent(comboBox, width / 4 - 10, height);
        } else {
            SpinnerNumberModel valueModel = new SpinnerNumberModel();
            switch (handledValue) {
                case SIZE:
                    valueModel.setMinimum(1);
                    valueModel.setMaximum(30);
                    valueModel.setValue(SIZE_FACTOR);
                    break;
                case MOBILITY:
                    valueModel.setMinimum(0.1);
                    valueModel.setMaximum(1.0);
                    valueModel.setStepSize(0.1);
                    valueModel.setValue(ConfigLib.MOBILITY);
                    break;
                case POPULATION:
                    valueModel.setMinimum(0.1);
                    valueModel.setMaximum(0.8);
                    valueModel.setStepSize(0.1);
                    valueModel.setValue(POPULATION_DENSITY);
                case INFECTIONONFIELD:
                    valueModel.setMinimum(0.1);
                    valueModel.setMaximum(0.8);
                    valueModel.setStepSize(0.1);
                    valueModel.setValue(PROBABILITY_OF_INFECTION_ON_FIELD);
                    break;
                case LETHALITY:
                    valueModel.setMinimum(0.01);
                    valueModel.setMaximum(0.9);
                    valueModel.setStepSize(0.01);
                    valueModel.setValue(ConfigLib.LETHALITY);
                    break;
                case DIRECTION:
                    valueModel.setMinimum(0.0);
                    valueModel.setMaximum(1.0);
                    valueModel.setStepSize(0.1);
                    valueModel.setValue(directionChange);
                    break;
                case LETHALITYPLUSFORRISKAGE:
                    valueModel.setMinimum(0.01);
                    valueModel.setMaximum(0.9);
                    valueModel.setStepSize(0.01);
                    valueModel.setValue(LETHALITY_PLUS_FOR_RISK_AGE);
                    break;
                case RISKAGE:
                    valueModel.setMinimum(10);
                    valueModel.setMaximum(90);
                    valueModel.setStepSize(1);
                    valueModel.setValue(RISK_AGE);
                    break;
                case INCUBATION:
                    valueModel.setMinimum(1l);
                    valueModel.setStepSize(1l);
                    valueModel.setValue(INCUBATION_TIME);
                    break;
                case ILLNESS:
                    valueModel.setMinimum(1l);
                    valueModel.setStepSize(1l);
                    valueModel.setValue(ILLNESS_DURATION);
                    break;
                case TIMESPEED:
                    valueModel.setMinimum(200);
                    valueModel.setStepSize(10);
                    valueModel.setValue(TIME_SPEED);
                    break;
                case TIMESPEEDSTEP:
                    valueModel.setMinimum(1);
                    valueModel.setMaximum(200);
                    valueModel.setStepSize(1);
                    valueModel.setValue(TIME_SPEED_STEP);
                    break;
            }
            valueModel.addChangeListener((e -> {
                switch (handledValue) {
                    case SIZE:
                        int newValue = (int) valueModel.getValue();
                        SIZE_FACTOR = newValue;
                        MAX_X = SIZE_FACTOR;
                        MAX_Y = (int) (SIZE_FACTOR * screenFactor);
                        Simulation.init();
                        break;
                    case POPULATION:
                        double newPop = (double) valueModel.getValue();
                        POPULATION_DENSITY = newPop;
                        Simulation.init();
                        break;
                    case MOBILITY:
                        double newMob = (double) valueModel.getValue();
                        ConfigLib.MOBILITY = newMob;
                        break;
                    case LETHALITY:
                        double newLet = (double) valueModel.getValue();
                        ConfigLib.LETHALITY = newLet;
                        break;
                    case LETHALITYPLUSFORRISKAGE:
                        double newLetPlus = (double) valueModel.getValue();
                        LETHALITY_PLUS_FOR_RISK_AGE = newLetPlus;
                        break;
                    case INFECTIONONFIELD:
                        double newInf = (double) valueModel.getValue();
                        PROBABILITY_OF_INFECTION_ON_FIELD = newInf;
                        break;
                    case ILLNESS:
                        long newIll = (long) valueModel.getValue();
                        ILLNESS_DURATION = newIll;
                        break;
                    case INCUBATION:
                        long newInc = (long) valueModel.getValue();
                        INCUBATION_TIME = newInc;
                        break;
                    case RISKAGE:
                        int newAge = (int) valueModel.getValue();
                        RISK_AGE = newAge;
                        break;
                    case TIMESPEED:
                        int newSpeed = (int) valueModel.getValue();
                        TIME_SPEED = newSpeed;
                        break;
                    case TIMESPEEDSTEP:
                        int newStep = (int) valueModel.getValue();
                        TIME_SPEED_STEP = newStep;
                        break;
                    case DIRECTION:
                        double newDir = (double) valueModel.getValue();
                        directionChange = newDir;
                        break;
                }
            }));
            JSpinner spinner = new JSpinner(valueModel);
            this.addComponent(spinner, width / 4 - 10, height);
        }
    }


    private void addComponent(Component component, int width, int height) {
        component.setPreferredSize(new Dimension(width, height));
        this.add(component);
    }
}
