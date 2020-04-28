package Gui.Map;

import process.Simulation;

import javax.swing.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static process.ConfigLib.MAX_X;
import static process.ConfigLib.MAX_Y;

public class MapPanel extends JPanel {
    private JLabel label;
    private List<GraphicField> fields = new ArrayList<>();

    public MapPanel() {
        super();
        this.setLayout(new GridLayout(MAX_X, MAX_Y));
//        label = new JLabel("Number of infected Persons: " + Simulation.getInfectedCount());
//        this.add(label);
        generateFields();
    }

    public void updateSimulation() {
        AtomicInteger i = new AtomicInteger();
        Simulation.getFields().forEach(field -> {
            this.fields.get(i.get()).update(field);
            i.getAndIncrement();
        });
//        label.setText("Number of infected Persons: " + Simulation.getInfectedCount());
    }

    private void generateFields() {
        Simulation.getFields().forEach(field ->{
            GraphicField graphicField = new GraphicField(field);
            this.fields.add(graphicField);
            this.add(graphicField);
        });
    }

    private void addLabel() {

    }
}
