package Gui.Settings;

import Gui.Settings.graph.GraphPanel;
import Gui.Settings.graph.LabeledGraphPanel;
import process.Simulation;
import process.time.TimeLine;

import javax.swing.*;
import java.awt.*;

public class SettingPanel extends JPanel {


    private LabeledGraphPanel totalGraph, relativeGraph;
    private ConfigPanel configPanel;


    public SettingPanel(int width, int height) {
        this.setPreferredSize(new Dimension(width, height));
        this.setVisible(true);
        this.addComponent(new JLabel("Settings"), width, 20);

        configPanel = new ConfigPanel(width, (height / 3));
        this.add(configPanel);

        totalGraph = new LabeledGraphPanel(TimeLine.getDeads(), TimeLine.getInfected(), TimeLine.getImmune(),
                false, width - 10, height / 4);
        relativeGraph = new LabeledGraphPanel(TimeLine.getDeads(), TimeLine.getInfected(), TimeLine.getImmune(),
                true, width - 10, height / 4);
//        this.addComponent(totalGraph, width - 10, height / 4);
//        this.addComponent(relativeGraph, width - 10, height / 4);
        this.add(totalGraph);
        this.add(relativeGraph);
    }


    private void updateSettings() {
        if (Simulation.getSystemRunning().get() || Simulation.isPaused() || Simulation.isFinished()) {
            totalGraph.setVisible(true);
            relativeGraph.setVisible(true);
            configPanel.setPreferredSize(new Dimension(this.getWidth(), (this.getHeight() / 3)));

        } else {
            configPanel.setPreferredSize(new Dimension(this.getWidth(), this.getHeight() - 100));
            totalGraph.setVisible(false);
            relativeGraph.setVisible(false);
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
