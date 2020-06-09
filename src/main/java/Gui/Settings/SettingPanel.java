package Gui.Settings;

import process.Simulation;
import process.time.TimeLine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class SettingPanel extends JPanel {


    private GraphPanel totalGraph, relativeGraph;
    private JButton startStopButton;


    public SettingPanel(int width, int height) {
        this.setPreferredSize(new Dimension(width, height));
//        this.setLayout(null);
        this.setVisible(true);
        this.addComponent(new JLabel("Settings"), width, 20);
        totalGraph = new GraphPanel(TimeLine.getDeads(), TimeLine.getInfected(), TimeLine.getImmune(), false);
        relativeGraph = new GraphPanel(TimeLine.getDeads(), TimeLine.getInfected(), TimeLine.getImmune(), true);
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
        this.addComponent(startStopButton, 200, 20);
        this.addComponent(totalGraph, width - 10, height / 3);
        this.addComponent(relativeGraph, width - 10, height / 3);
    }


    private void updateSettings() {
        if (Simulation.getSystemRunning().get() || Simulation.isPaused()) {
            if (Simulation.isPaused()) {
                startStopButton.setText("Start");
            } else {
                startStopButton.setText("Pause");
            }
            startStopButton.setVisible(true);
            totalGraph.setVisible(true);
            relativeGraph.setVisible(true);
        } else {
            if (Simulation.isFinished()) {
                totalGraph.setVisible(true);
                relativeGraph.setVisible(true);
                startStopButton.setText("Simulation zurücksetzen");
                startStopButton.setVisible(true);
            } else {
                startStopButton.setText("Simulation starten");
                totalGraph.setVisible(false);
                relativeGraph.setVisible(false);
            }
        }
    }

    public void updateSimulation() {
        totalGraph.update(TimeLine.getDeads(), TimeLine.getInfected(), TimeLine.getImmune());
        relativeGraph.update(TimeLine.getDeads(), TimeLine.getInfected(), TimeLine.getImmune());
        updateSettings();
    }

    private void addComponent(Component component, int width, int height) {
        component.setPreferredSize(new Dimension(width, height));
        this.add(component);
    }
}
