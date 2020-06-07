package Gui.Map;

import lombok.Data;
import model.Person;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;

import static process.ConfigLib.TIME_SPEED;

//@AllArgsConstructor
@Data
public class GraphicPerson extends JPanel {
    private Person person;
    private int radius;
    private int maxX;
    private int maxY;

    //
    public GraphicPerson(Person person, int radius, int maxX, int maxY) {
        this.person = person;
        this.radius = radius;
        this.maxX = maxX;
        this.maxY = maxY;
        this.setVisible(true);
        this.setLayout(null);
        if (person.getRecentPosition() != null) {
            this.setBounds(person.getRecentPosition().x, person.getRecentPosition().y, radius * 2, radius * 2);
            animatedMove(this, createRandomPosition());
        } else {
            setRandomBounds();
        }
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(this.person.isInfected() ? Color.RED : this.person.isImmune() ? Color.GREEN : Color.GRAY);
        g2d.fill(new Ellipse2D.Double(0, 0, radius * 2, radius * 2));
    }

    private void setRandomBounds() {
        Point randomPosition = createRandomPosition();
        this.person.setRecentPosition(randomPosition);
        this.setBounds(randomPosition.x, randomPosition.y, radius * 2, radius * 2);
    }

    private Point createRandomPosition() {
        return new Point((int) (Math.random() * Math.abs(maxX - radius - 2)),
                (int) (Math.random() * Math.abs(maxY - radius - 2)));
    }


    public void update(Person person) {
        this.person = person;
        if (this.person.getRecentPosition() != null) {
            this.setBounds(person.getRecentPosition().x, person.getRecentPosition().y, radius * 2, radius * 2);
            Point randomPos = createRandomPosition();
            animatedMove(this, randomPos);
            this.person.setRecentPosition(randomPos);
        }
        this.revalidate();
        this.repaint();
    }

    public static void animatedMove(GraphicPerson person, Point newPoint) {
        SwingUtilities.invokeLater(() -> {
            Rectangle compBounds = person.getBounds();
            int frames = 60;

            Point oldPoint = new Point(compBounds.x, compBounds.y),
                    animFrame = new Point((newPoint.x - oldPoint.x) / frames,
                            (newPoint.y - oldPoint.y) / frames);

            new Timer(TIME_SPEED / frames, new ActionListener() {
                int currentFrame = 0;

                public void actionPerformed(ActionEvent e) {
                    person.setBounds(oldPoint.x + (animFrame.x * currentFrame),
                            oldPoint.y + (animFrame.y * currentFrame),
                            compBounds.width,
                            compBounds.height);

                    if (currentFrame != frames) {
                        currentFrame++;
                    } else {
                        ((Timer) e.getSource()).stop();
                    }
                }
            }).start();
        });
    }
}
