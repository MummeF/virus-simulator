package Gui.Map;

import lombok.Data;
import model.Field;
import process.Simulation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


@Data
public class GraphicField extends JTextField {
    private Field field;


    public GraphicField(Field field) {
        super("" + field.getPersons().size());
        if (field.isAccessible()) {
            if (field.anyInfectedPersonOnIt()) {
                this.setBackground(Color.red);
            } else {
                this.setBackground(Color.white);
            }
        } else {
            this.setBackground(Color.gray);
        }
        this.setVisible(true);
        this.setFocusable(false);

        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem decline = new JMenuItem("Decline");
        decline.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Simulation.declineField(field.getX(), field.getY());
            }
        });
        JMenuItem allow = new JMenuItem("Allow");
        decline.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Simulation.allowField(field.getX(), field.getY());
            }
        });
        popupMenu.add(decline);
        popupMenu.add(allow);
        SwingUtilities.invokeLater(() -> this.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        popupMenu.show(GraphicField.this, e.getX(), e.getY());
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        popupMenu.show(GraphicField.this, e.getX(), e.getY());
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        popupMenu.show(GraphicField.this, e.getX(), e.getY());
                    }
                })
        );

    }

    public void update(Field field) {
        this.field = field;
        if (field.isAccessible()) {
            if (field.anyInfectedPersonOnIt()) {
                this.setBackground(Color.red);
            } else {
                this.setBackground(Color.white);
            }
        } else {
            this.setBackground(Color.gray);
        }
        this.setText("" + field.getPersons().size());
    }
}
