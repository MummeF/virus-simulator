package Gui.Settings;

import process.Simulation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class ConfigPanel extends JPanel {
    private JButton startStopButton, resetButton;
    private SingleInputPanel size, mobility, population, riskAge, lethality, lethalityPlusForRiskage,
            inkubationTime, illnessTime, infection;

    public ConfigPanel(int width, int height) {
        this.setPreferredSize(new Dimension(width, height));
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
        this.size = new SingleInputPanel(width, 30, SingleInputPanel.SIZE, "Größenfaktor:");
        this.add(size);
        this.population = new SingleInputPanel(width, 30, SingleInputPanel.POPULATION, "Bevölkerungsdichte pro Feld:");
        this.add(population);
        this.mobility = new SingleInputPanel(width, 30, SingleInputPanel.MOBILITY, "Mobilitätsfaktor:");
        this.add(mobility);
        this.riskAge = new SingleInputPanel(width, 30, SingleInputPanel.RISKAGE, "Risikoalter:");
        this.add(riskAge);
        this.inkubationTime = new SingleInputPanel(width, 30, SingleInputPanel.INCUBATION, "Inkubationszeit:");
        this.add(inkubationTime);
        this.illnessTime = new SingleInputPanel(width, 30, SingleInputPanel.ILLNESS, "Krankheitsdauer:");
        this.add(illnessTime);
        this.lethality = new SingleInputPanel(width, 30, SingleInputPanel.LETHALITY, "Tödlichkeitsfaktor pro Tag:");
        this.add(lethality);
        this.lethalityPlusForRiskage = new SingleInputPanel(width, 30, SingleInputPanel.LETHALITYPLUSFORRISKAGE, "Erhöhter Tödlichkeitsfaktor für Risikogruppen:");
        this.add(lethalityPlusForRiskage);
        this.infection = new SingleInputPanel(width, 30, SingleInputPanel.INFECTIONONFIELD, "Wahrscheinlichkeit der Infektion bei Kontakt:");
        this.add(infection);


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
            inkubationTime.setVisible(true);
            illnessTime.setVisible(true);
            lethality.setVisible(true);
            lethalityPlusForRiskage.setVisible(true);
            infection.setVisible(true);
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
                inkubationTime.setVisible(true);
                illnessTime.setVisible(true);
                lethality.setVisible(true);
                lethalityPlusForRiskage.setVisible(true);
                infection.setVisible(true);
            } else {
                startStopButton.setText("Simulation starten");
                size.setVisible(true);
                mobility.setVisible(true);
                population.setVisible(true);
                riskAge.setVisible(true);
                inkubationTime.setVisible(true);
                illnessTime.setVisible(true);
                lethality.setVisible(true);
                lethalityPlusForRiskage.setVisible(true);
                infection.setVisible(true);
            }
        }
    }

    private void addComponent(Component component, int width, int height) {
        component.setPreferredSize(new Dimension(width, height));
        this.add(component);
    }
}
