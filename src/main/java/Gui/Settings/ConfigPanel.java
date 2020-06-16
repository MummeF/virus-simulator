package Gui.Settings;

import process.Simulation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class ConfigPanel extends JPanel {
    private JButton startStopButton, resetButton;
    private JLabel field, virus, system;
    private SingleInputPanel size, mobility, direction, population, riskAge, lethality, lethalityPlusForRiskage,
            incubationTime, illnessTime, infection, animatedMove, timeSpeed, timeSpeedStep, timeUnit;

    public ConfigPanel(int width) {
        this.setPreferredSize(new Dimension(width, 500));
        resetButton = new JButton("Zurücksetzen");
        resetButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Simulation.init();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        startStopButton = new JButton("Simulation starten");
        startStopButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (Simulation.getSystemRunning().get() || Simulation.isPaused()) {
                    if (Simulation.isPaused()) {
                        Simulation.start();
                    } else {
                        Simulation.pause();
                        updateSettings();
                    }
                } else {
                    if (Simulation.isFinished()) {
                        Simulation.init();
                    } else {
                        Simulation.start();
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        this.addComponent(startStopButton, width / 2, 20);
        this.addComponent(resetButton, width / 4, 20);
        resetButton.setVisible(false);
        field = new JLabel("Bewegung und Bevölkerung", SwingConstants.LEFT);
        field.setFont(new Font(field.getFont().getName(), Font.BOLD, field.getFont().getSize()));
        this.addComponent(field, width, 20);
        this.size = new SingleInputPanel(width, 30, SingleInputPanel.SIZE, "Größenfaktor:");
        this.add(size);
        this.population = new SingleInputPanel(width, 30, SingleInputPanel.POPULATION, "Bevölkerungsdichte pro Feld:");
        this.add(population);
        this.mobility = new SingleInputPanel(width, 30, SingleInputPanel.MOBILITY, "Mobilitätsfaktor:");
        this.add(mobility);
        this.direction = new SingleInputPanel(width, 30, SingleInputPanel.DIRECTION, "Wahrscheinlichkeit zum Richtungswechsel:");
        this.add(direction);
        virus = new JLabel("Krankheit", SwingConstants.LEFT);
        virus.setFont(new Font(virus.getFont().getName(), Font.BOLD, virus.getFont().getSize()));
        this.addComponent(virus, width, 20);
        this.riskAge = new SingleInputPanel(width, 30, SingleInputPanel.RISKAGE, "Risikoalter:");
        this.add(riskAge);
        this.incubationTime = new SingleInputPanel(width, 30, SingleInputPanel.INCUBATION, "Inkubationszeit:");
        this.add(incubationTime);
        this.illnessTime = new SingleInputPanel(width, 30, SingleInputPanel.ILLNESS, "Krankheitsdauer:");
        this.add(illnessTime);
        this.lethality = new SingleInputPanel(width, 30, SingleInputPanel.LETHALITY, "Tödlichkeitsfaktor pro Tag:");
        this.add(lethality);
        this.lethalityPlusForRiskage = new SingleInputPanel(width, 30, SingleInputPanel.LETHALITYPLUSFORRISKAGE, "Erhöhter Tödlichkeitsfaktor für Risikogruppen:");
        this.add(lethalityPlusForRiskage);
        this.infection = new SingleInputPanel(width, 30, SingleInputPanel.INFECTIONONFIELD, "Wahrscheinlichkeit der Infektion bei Kontakt:");
        this.add(infection);
        system = new JLabel("System", SwingConstants.LEFT);
        system.setFont(new Font(system.getFont().getName(), Font.BOLD, system.getFont().getSize()));
        this.addComponent(system, width, 20);
        this.animatedMove = new SingleInputPanel(width, 30, SingleInputPanel.ANIMATION, "Animierte Bewegung:");
        this.add(animatedMove);
        this.timeSpeed = new SingleInputPanel(width, 30, SingleInputPanel.TIMESPEED, "Dauer der Züge:");
        this.add(timeSpeed);
        this.timeSpeedStep = new SingleInputPanel(width, 30, SingleInputPanel.TIMESPEEDSTEP, "Vergangene Zeiteinheit pro Zug:");
        this.add(timeSpeedStep);
        this.timeUnit = new SingleInputPanel(width, 30, SingleInputPanel.TIMEUNIT, "Zeiteinheit:");
        this.add(timeUnit);

    }

    public void updateSimulation() {
        updateSettings();
    }

    private void updateSettings() {
        if (Simulation.getSystemRunning().get() || Simulation.isPaused()) {
            if (Simulation.isPaused()) {
                startStopButton.setText("Start");
                startStopButton.setVisible(true);
                startStopButton.setPreferredSize(new Dimension(this.getWidth() / 4, 20));
                resetButton.setVisible(true);
            } else {
                startStopButton.setText("Pause");
                startStopButton.setVisible(true);
                startStopButton.setPreferredSize(new Dimension(this.getWidth() / 2, 20));
                resetButton.setVisible(false);
            }
            startStopButton.setVisible(true);
            size.setVisible(false);
            mobility.setVisible(true);
            population.setVisible(false);
            riskAge.setVisible(true);
            incubationTime.setVisible(true);
            illnessTime.setVisible(true);
            lethality.setVisible(true);
            lethalityPlusForRiskage.setVisible(true);
            infection.setVisible(true);
            animatedMove.setVisible(true);
            infection.setVisible(true);
            timeSpeed.setVisible(true);
            timeSpeedStep.setVisible(true);
            timeUnit.setVisible(false);
        } else {
            startStopButton.setVisible(true);
            startStopButton.setPreferredSize(new Dimension(this.getWidth() / 2, 20));
            resetButton.setVisible(false);
            if (Simulation.isFinished()) {
                startStopButton.setText("Simulation zurücksetzen");
                startStopButton.setVisible(true);
                size.setVisible(false);
                mobility.setVisible(true);
                population.setVisible(false);
                riskAge.setVisible(true);
                incubationTime.setVisible(true);
                illnessTime.setVisible(true);
                lethality.setVisible(true);
                lethalityPlusForRiskage.setVisible(true);
                infection.setVisible(true);
                animatedMove.setVisible(true);
                infection.setVisible(true);
                timeSpeed.setVisible(true);
                timeSpeedStep.setVisible(true);
                timeUnit.setVisible(false);
            } else {
                startStopButton.setText("Simulation starten");
                size.setVisible(true);
                mobility.setVisible(true);
                population.setVisible(true);
                riskAge.setVisible(true);
                incubationTime.setVisible(true);
                illnessTime.setVisible(true);
                lethality.setVisible(true);
                lethalityPlusForRiskage.setVisible(true);
                infection.setVisible(true);
                animatedMove.setVisible(true);
                infection.setVisible(true);
                timeSpeed.setVisible(true);
                timeSpeedStep.setVisible(true);
                timeUnit.setVisible(true);
            }
        }
    }

    private void addComponent(Component component, int width, int height) {
        component.setPreferredSize(new Dimension(width, height));
        this.add(component);
    }
}
