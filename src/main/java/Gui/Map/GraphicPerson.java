package Gui.Map;

import model.Person;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

//@AllArgsConstructor
public class GraphicPerson extends JPanel {
    private Person person;
    private int radius;

    //
    public GraphicPerson(Person person, int radius, int maxX, int maxY) {
        this.person = person;
        this.radius = radius;
        this.setVisible(true);
        this.setLayout(null);
        this.setBounds((int) (Math.random() * Math.abs(maxX - radius - 2)), (int) (Math.random() * Math.abs(maxY - radius - 1)), radius * 2, radius * 2);
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
        g2d.setColor(this.person.isInfected() ? Color.RED : this.person.isImmune() ? Color.GREEN : Color.GRAY);
        Ellipse2D.Double kreis = new Ellipse2D.Double(0, 0, radius * 2, radius * 2);
        g2d.fill(kreis);
    }

    public void update(Person person) {
        this.person = person;
        this.revalidate();
        this.repaint();
    }
}
