package Gui.Map;

import lombok.Data;
import model.Field;
import model.Person;
import process.Simulation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;


@Data
public class GraphicField extends JPanel {
    private Field field;


    public GraphicField(Field field) {
        this.setVisible(true);
        this.setFocusable(false);
        this.setLayout(null);
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem decline = new JMenuItem("Decline");
        decline.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                decline();
            }
        });
        JMenuItem allow = new JMenuItem("Allow");
        allow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                allow();
            }
        });
        popupMenu.add(decline);
        popupMenu.add(allow);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                popupMenu.show(GraphicField.this, e.getX(), e.getY());
            }
        });
        update(field);

    }

    private void decline() {
        this.setBackground(Color.lightGray);
        Simulation.declineField(field.getX(), field.getY());
    }

    private void allow() {
        this.setBackground(Color.lightGray);
        Simulation.allowField(field.getX(), field.getY());
    }

    public void update(Field field) {
        this.field = field;
        if (field.isAccessible()) {
            this.setBackground(Color.WHITE);
        } else {
            this.setBackground(Color.gray);
        }
        int personIndex = 0;
        List<Person> persons = field.getPersons();
        for (int i = 0; i < this.getComponents().length; i++) {
            Component component = getComponent(i);
            if (component instanceof GraphicPerson) {
                if (personIndex < persons.size()) {
                    ((GraphicPerson) getComponent(i)).update(persons.get(personIndex));
                    personIndex++;
                } else {
                    this.remove(i);
                }
            }
        }
        for (; personIndex < persons.size(); personIndex++) {
            GraphicPerson toAdd = new GraphicPerson(persons.get(personIndex), 5, this.getWidth(), this.getHeight());
            this.add(toAdd);
        }
        this.revalidate();
        this.repaint();
    }
}
