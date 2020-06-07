package Gui;

import Gui.Map.MapPanel;
import Gui.Settings.SettingPanel;
import process.Simulation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Gui extends JFrame {
    private MapPanel mapPanel;
    private SettingPanel settingPanel;

    public Gui() {
        super("Virus-Simulator");
        this.setLayout(new BorderLayout());
        Simulation.setGui(this);
        this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        this.setVisible(true);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        mapPanel = new MapPanel();
        this.add(mapPanel, BorderLayout.CENTER);

        settingPanel = new SettingPanel(this.getWidth() / 4, this.getHeight());
        this.add(settingPanel, BorderLayout.WEST);

        Simulation.init();
        Simulation.start();
    }

    public void updateSimulation() {
        if (mapPanel != null) {
            this.mapPanel.updateSimulation();
        }
        if (settingPanel != null) {
            this.settingPanel.updateSimulation();
        }
    }
}
