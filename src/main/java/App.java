
import Gui.Gui;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGui());
//        Simulation s = new Simulation();
//        s.run();

    }

    private static void createAndShowGui() {
        Gui gui = new Gui();
    }
}
