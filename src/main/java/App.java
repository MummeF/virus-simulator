
import Gui.Gui;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGui());
    }

    private static void createAndShowGui() {
        new Gui();
    }
}
