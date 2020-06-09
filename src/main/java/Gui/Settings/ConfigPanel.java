package Gui.Settings;

import process.Simulation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ConfigPanel extends JPanel {
    private JButton startStopButton;

    public ConfigPanel(int width, int height) {
        this.setPreferredSize(new Dimension(width, height));
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

    }

    public void updateSimulation() {
        updateSettings();
    }

    private void updateSettings() {
        if (Simulation.getSystemRunning().get() || Simulation.isPaused()) {
            if (Simulation.isPaused()) {
                startStopButton.setText("Start");
            } else {
                startStopButton.setText("Pause");
            }
            startStopButton.setVisible(true);
        } else {
            if (Simulation.isFinished()) {
                startStopButton.setText("Simulation zurücksetzen");
                startStopButton.setVisible(true);
            } else {
                startStopButton.setText("Simulation starten");
            }
        }
    }

    private void addComponent(Component component, int width, int height) {
        component.setPreferredSize(new Dimension(width, height));
        this.add(component);
    }
}
