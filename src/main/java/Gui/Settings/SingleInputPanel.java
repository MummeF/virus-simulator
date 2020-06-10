package Gui.Settings;

import process.ConfigLib;
import process.Simulation;

import javax.swing.*;
import java.awt.*;

import static process.ConfigLib.*;

public class SingleInputPanel extends JPanel {
    public static final String SIZE = "size-factor", MOBILITY = "mobility",
            POPULATION = "population", RISKAGE = "riskage", LETHALITY = "lethality",
            LETHALITYPLUSFORRISKAGE = "lethality-plus-for-risk-age", INCUBATION = "incubation",
            ILLNESS = "illness", INFECTIONONFIELD = "infection-on-field";


    private String handledValue;

    public SingleInputPanel(int width, int height, String handledValue, String descriptionText) {
        this.handledValue = handledValue;
        this.setPreferredSize(new Dimension(width, height));
        this.addComponent(new JLabel(descriptionText, SwingConstants.RIGHT), width / 4 * 3 - 10, height);
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
            }
        }));
        JSpinner spinner = new JSpinner(valueModel);
        this.addComponent(spinner, width / 4 - 10, height);
    }

    private void addComponent(Component component, int width, int height) {
        component.setPreferredSize(new Dimension(width, height));
        this.add(component);
    }
}
