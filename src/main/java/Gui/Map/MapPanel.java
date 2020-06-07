package Gui.Map;

import process.Simulation;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;

import static process.ConfigLib.MAX_X;
import static process.ConfigLib.MAX_Y;

public class MapPanel extends JPanel {


    private int fieldWidth, fieldHeight;

    public MapPanel() {
        super();
        this.setLayout(null);
        this.setBackground(Color.WHITE);
        fieldWidth = this.getWidth() / MAX_X;
        fieldHeight = this.getHeight() / MAX_Y;
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        updateSimulation();
    }

    public void updateSimulation() {
        this.fieldWidth = this.getWidth() / MAX_X;
        this.fieldHeight = this.getHeight() / MAX_Y;
        AtomicInteger i = new AtomicInteger();
        Simulation.getFields().forEach(field -> {
            field.getPersons().forEach(person -> {
                if (i.get() < this.getComponentCount()) {
                    person.updateGraphicPerson(translateX(field.getX()),
                            translateY(field.getY()),
                            this.fieldWidth,
                            this.fieldHeight);
                } else {
                    GraphicPerson graphicPerson = new GraphicPerson(person, 5,
                            translateX(field.getX()),
                            translateY(field.getY()),
                            this.fieldWidth,
                            this.fieldHeight);
                    person.setGraphicPerson(graphicPerson);
                    this.add(graphicPerson);
                }
                i.getAndIncrement();
            });
        });
        Simulation.getDeads().forEach(person -> this.remove(person.getGraphicPerson()));
        this.revalidate();
        this.repaint();
    }


    private int translateX(int x) {
        return this.fieldWidth * x;
    }

    private int translateY(int y) {
        return this.fieldHeight * y;
    }


}
