package Gui.Map;

import model.Person;
import process.Simulation;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static process.ConfigLib.MAX_X;
import static process.ConfigLib.MAX_Y;

public class MapPanel extends JPanel {
    private List<GraphicField> fields = new ArrayList<>();

    public MapPanel() {
        super();
        this.setLayout(new GridLayout(MAX_Y, MAX_X));
        generateFields();
    }

    public void updateSimulation() {
        AtomicInteger i = new AtomicInteger();
        Simulation.getFields().forEach(field -> {
            this.fields.get(i.get()).update(field);
            i.getAndIncrement();
        });
        this.revalidate();
        this.repaint();
    }

    private void generateFields() {
        Simulation.getFields().forEach(field -> {
            GraphicField graphicField = new GraphicField(field);
            this.fields.add(graphicField);
            this.add(graphicField);
        });
    }
}
