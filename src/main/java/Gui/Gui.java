package Gui;

import process.Simulation;

import javax.swing.*;
import java.awt.*;

public class Gui extends JFrame {
    public Gui(){
        super("Virus-Simulator");
        MapPanel mapPanel = new MapPanel();
        Simulation.init(mapPanel);
        this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setContentPane(mapPanel);
        Simulation.run();
    }
}
