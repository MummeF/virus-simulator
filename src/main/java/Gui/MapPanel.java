package Gui;

import model.Field;
import process.Simulation;

import javax.swing.*;
import java.util.List;

public class MapPanel extends JPanel {
    private List<Field> fields;
    private JLabel label;

    public MapPanel() {
        super();
        this.fields = Simulation.getFields();
        long numberOfPersons = fields.stream().filter(field -> field.getPersons().size() > 0)
                .map(field -> field.getPersons().size())
                .count();
        label = new JLabel("Number of infected Persons: "+Simulation.getInfectedCount());
        this.add(label);
    }

    public void updateSimulation(){
        this.fields = Simulation.getFields();
        long numberOfPersons = fields.stream().filter(field -> field.getPersons().size() > 0)
                .map(field -> field.getPersons().size())
                .count();
        label.setText("Number of infected Persons: "+Simulation.getInfectedCount());
    }
}
