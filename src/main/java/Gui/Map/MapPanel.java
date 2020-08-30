package Gui.Map;

import model.Border;
import process.Simulation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.List;

import static process.ConfigLib.MAX_X;
import static process.ConfigLib.MAX_Y;

public class MapPanel extends JPanel {


    private int fieldWidth, fieldHeight;

    //Helper for Mouse
    private Point startPoint;
    private int borderTime = 0;

    public MapPanel() {
        super();
        this.setLayout(null);
        this.setBackground(Color.WHITE);
        fieldWidth = this.getWidth() / MAX_X;
        fieldHeight = this.getHeight() / MAX_Y;
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Point endPoint = e.getPoint();
                if(Simulation.borderSettingAllowed()) {
                    Simulation.addBorders(findBorders(startPoint, endPoint), borderTime);
                }
                repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
            }
        });

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                startPoint = e.getPoint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                startPoint = null;
                repaint();
                borderTime++;
            }
        });
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

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        g.setColor(Color.BLACK);
        if (Simulation.getBorders() != null) {
            Simulation.getBorders().stream().forEach(border -> paintBorder(border, g));
        }
    }

    private void paintBorder(Border border, Graphics g) {
        if (border.getX1() == border.getX2()) {
            //horizontal
            g.drawLine(border.getX1() * fieldWidth, border.getY2() * fieldHeight,
                    (border.getX1() + 1) * fieldWidth, border.getY2() * fieldHeight);
        } else {
            //vertical
            g.drawLine( (border.getX1() +1)* fieldWidth, border.getY1() * fieldHeight,
                    (border.getX1() +1) * fieldWidth, (border.getY1() + 1) * fieldHeight);
        }

    }


    private int translateX(int x) {
        return this.fieldWidth * x;
    }

    private int translateY(int y) {
        return this.fieldHeight * y;
    }


    public void resetSimulation() {
        this.removeAll();
        this.revalidate();
        this.repaint();
    }

    private List<Border> findBorders(Point mouseStart, Point mouseEnd) {
        Point startField = getField(mouseStart);
        Point endField = getField(mouseEnd);
        List<Border> borders = new ArrayList<>();
        int startX = (int) startField.getX();
        int startY = (int) startField.getY();
        int endX = (int) endField.getX();
        int endY = (int) endField.getY();
        if (startX <= endX && startX != MAX_X - 1) {
            for (; startX <=  endX; startX++) {
                borders.add(new Border(startX, startX, startY, startY + 1, borderTime));
            }
            startX --;
        } else {
            for (; startX >= endX; startX--) {
                borders.add(new Border(startX, startX, startY, startY + 1, borderTime));
            }
        }
        if (startY <= endY && startY != MAX_Y - 1) {
            for (; startY <= endY; startY++) {
                borders.add(new Border(startX, startX + 1, startY +1 , startY + 1, borderTime));
            }
        } else {
            for (; startY >= endY; startY--) {
                borders.add(new Border(startX, startX + 1, startY, startY, borderTime));
            }
        }
        return borders;
    }

    private Point getField(Point position) {
        try {
            int x = (position.x) / this.fieldWidth;
            if (x == MAX_X) {
                x = MAX_X - 1;
            }
            int y = (position.y) / this.fieldHeight;
            if (y == MAX_Y) {
                y = MAX_Y - 1;
            }
            return new Point(x, y);
        } catch (Exception e) {
            return new Point(0, 0);
        }
    }
}
