package Gui.Map;

import model.Person;
import process.Simulation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static process.ConfigLib.*;

public class MapPanel extends JPanel {
    private GraphicField[][] fields = new GraphicField[MAX_X][MAX_Y];

    public MapPanel() {
        super();
        this.setLayout(new GridLayout(MAX_X, MAX_Y));

        generateFields();
    }

    public void updateSimulation() {
        Simulation.getFields().forEach(field -> {
            this.fields[field.getX()][field.getY()].update(field);
        });
        this.revalidate();
        this.repaint();
    }



    private void generateFields() {
        Simulation.getFields().forEach(field -> {
            GraphicField graphicField = new GraphicField(field);
            this.fields[field.getX()][field.getY()] = graphicField;
            this.add(graphicField);
        });
    }
}
