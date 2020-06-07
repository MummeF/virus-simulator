package Gui.Map;

import lombok.Data;
import model.Person;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;

import static process.ConfigLib.ANIMATED_MOVE;
import static process.ConfigLib.TIME_SPEED;


@Data
public class GraphicPerson extends JPanel {
    private Person person;
    private int radius;
    private int width;
    private int height;
    private int possibleX;
    private int possibleY;


    public GraphicPerson(Person person, int radius, int x, int y, int width, int height) {
        this.person = person;
        this.radius = radius;
        this.possibleX = x;
        this.possibleY = y;
        this.width = width;
        this.height = height;
        this.setVisible(true);
        this.setLayout(null);
        setRandomBounds();
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
//        g2d.setColor(Color.white);
//        g2d.fillRect(0, 0, radius * 2, radius * 2);
        g2d.setColor(this.person.isInfected() ? Color.RED : this.person.isImmune() ? Color.GREEN : Color.GRAY);
        g2d.fill(new Ellipse2D.Double(0, 0, radius * 2, radius * 2));
    }

    private void setRandomBounds() {
        Point randomPosition = createRandomPosition();
        this.person.setRecentPosition(randomPosition);
        this.setBounds(randomPosition.x, randomPosition.y, radius * 2, radius * 2);
    }

    private Point createRandomPosition() {
        return new Point(this.possibleX + (int) (Math.random() * Math.abs(width - radius - 2)),
                this.possibleY + (int) (Math.random() * Math.abs(height - radius - 2)));
    }


    public void update(Person person, int x, int y, int width, int height) {
        this.possibleX = x;
        this.possibleY = y;
        this.width = width;
        this.height = height;
        this.person = person;
        if (this.person.getRecentPosition() != null) {
            if (!isInField(this.person.getRecentPosition())) {
                if (ANIMATED_MOVE) {
                    Point randomPos = createRandomPosition();
                    animatedMove(this, randomPos);
                    this.person.setRecentPosition(randomPos);
                } else {
                    setRandomBounds();
                }
            }
        } else {
            setRandomBounds();
        }
        this.revalidate();
        this.repaint();
    }

    private boolean isInField(Point recentPosition) {
        return (recentPosition.x >= this.possibleX && recentPosition.x <= this.possibleX + this.width)
                && (recentPosition.y >= this.possibleY && recentPosition.y <= this.possibleY + this.height);
    }

    public static void animatedMove(GraphicPerson person, Point newPoint) {
        SwingUtilities.invokeLater(() -> {
            Rectangle compBounds = person.getBounds();
            int frames = 60;

            Point oldPoint = new Point(compBounds.x, compBounds.y),
                    animFrame = new Point((newPoint.x - oldPoint.x) / frames,
                            (newPoint.y - oldPoint.y) / frames);

            new Timer((TIME_SPEED / 3 * 2) / frames, new ActionListener() {
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
