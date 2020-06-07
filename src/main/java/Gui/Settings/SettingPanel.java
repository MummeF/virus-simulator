package Gui.Settings;

import process.time.TimeLine;

import javax.swing.*;
import java.awt.*;

public class SettingPanel extends JPanel {
    private GraphPanel totalGraph, relativeGraph;

    public SettingPanel(int width, int height) {
        this.setPreferredSize(new Dimension(width, height));
        this.setLayout(new GridLayout(0, 1));
        this.setVisible(true);
        totalGraph = new GraphPanel(TimeLine.getDeads(), TimeLine.getInfected(), TimeLine.getImmune(), false);
        this.add(totalGraph);
        relativeGraph = new GraphPanel(TimeLine.getDeads(), TimeLine.getInfected(), TimeLine.getImmune(), true);
        this.add(relativeGraph);
    }

    public void updateSimulation() {
        totalGraph.update(TimeLine.getDeads(), TimeLine.getInfected(), TimeLine.getImmune());
        relativeGraph.update(TimeLine.getDeads(), TimeLine.getInfected(), TimeLine.getImmune());
    }
}
