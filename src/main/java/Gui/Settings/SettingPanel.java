package Gui.Settings;

import process.Simulation;
import process.time.TimeLine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class SettingPanel extends JPanel {


    private GraphPanel totalGraph, relativeGraph;
    private ConfigPanel configPanel;


    public SettingPanel(int width, int height) {
        this.setPreferredSize(new Dimension(width, height));
        this.setVisible(true);
        this.addComponent(new JLabel("Settings"), width, 20);

        configPanel = new ConfigPanel(width, height / 3 - 20);
        this.add(configPanel);

        totalGraph = new GraphPanel(TimeLine.getDeads(), TimeLine.getInfected(), TimeLine.getImmune(), false);
        relativeGraph = new GraphPanel(TimeLine.getDeads(), TimeLine.getInfected(), TimeLine.getImmune(), true);
        this.addComponent(totalGraph, width - 10, height / 3);
        this.addComponent(relativeGraph, width - 10, height / 3);
    }


    private void updateSettings() {
        if (Simulation.getSystemRunning().get() || Simulation.isPaused()) {
            totalGraph.setVisible(true);
            relativeGraph.setVisible(true);
        } else {
            if (Simulation.isFinished()) {
                totalGraph.setVisible(true);
                relativeGraph.setVisible(true);
            } else {
                totalGraph.setVisible(false);
                relativeGraph.setVisible(false);
            }
        }
    }

    public void updateSimulation() {
        totalGraph.update(TimeLine.getDeads(), TimeLine.getInfected(), TimeLine.getImmune());
        relativeGraph.update(TimeLine.getDeads(), TimeLine.getInfected(), TimeLine.getImmune());
        configPanel.updateSimulation();
        updateSettings();
    }

    private void addComponent(Component component, int width, int height) {
        component.setPreferredSize(new Dimension(width, height));
        this.add(component);
    }
}
