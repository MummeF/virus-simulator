package Gui;

import Gui.Map.MapPanel;
import process.Simulation;

import javax.swing.*;
import java.awt.*;

public class Gui extends JFrame {
    public Gui(){
        super("Virus-Simulator");
        MapPanel mapPanel = new MapPanel();
        this.setLayout(new GridLayout(1,2));
        Simulation.init(mapPanel);
        this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        this.setVisible(true);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setContentPane(mapPanel);
        Simulation.run();
    }
}
