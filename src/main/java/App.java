
import process.Simulation;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> createAndShowGui());
        Simulation s = new Simulation();
        s.run();

    }
//
//    private static void createAndShowGui(){
//        Gui gui = new Gui(900,900);
//        JFrame frame = new JFrame("Virus-Simulator");
//        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        frame.setLayout(null);
//        frame.setSize(900,900);
//        frame.getContentPane().add(gui);
//        frame.setLocationByPlatform(true);
//        frame.setVisible(true);
//
//    }
}
