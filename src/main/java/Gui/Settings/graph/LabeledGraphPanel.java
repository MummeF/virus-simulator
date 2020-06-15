package Gui.Settings.graph;

import process.time.TimeValue;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LabeledGraphPanel extends JPanel {
    private GraphPanel graphPanel;

    public LabeledGraphPanel(List<TimeValue> deads, List<TimeValue> infected, List<TimeValue> immune,
                             boolean relative, int width, int height) {
        this.setPreferredSize(new Dimension(width, height));
        this.add(new JLabel("Geheilte:"));
        this.add(new RectPanel(relative, (width - 30) / (GraphPanel.getRELATIVE_NUMBER() * 3), 20, Color.GREEN));
        this.add(new JLabel("Infizierte:"));
        this.add(new RectPanel(relative, (width - 30) / (GraphPanel.getRELATIVE_NUMBER() * 3), 20, Color.RED));
        this.add(new JLabel("Tote:"));
        this.add(new RectPanel(relative, (width - 30) / (GraphPanel.getRELATIVE_NUMBER() * 3), 20, Color.BLACK));

        this.graphPanel = new GraphPanel(deads, infected, immune, relative, width, height - 30);
        this.add(graphPanel);


    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
    }

    public void update(List<TimeValue> deads, List<TimeValue> infected, List<TimeValue> immune) {
        this.graphPanel.update(deads, infected, immune);
    }


    private void addComponent(Component component, int width, int height) {
        component.setPreferredSize(new Dimension(width, height));
        this.add(component);
    }

    private class RectPanel extends JPanel {
        private boolean relative;
        private Color color;

        public RectPanel(boolean relative, int width, int height, Color color) {
            this.relative = relative;
            this.color = color;
            this.setLayout(null);
            this.setPreferredSize(new Dimension(width, height));
        }

        @Override
        public void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.WHITE);
            g2.fillRect(0, 0, this.getWidth(), this.getHeight());
            g2.setColor(this.color);
            if (relative) {
                g2.fillRect(0, 0, this.getWidth(), this.getHeight());
            } else {
                g2.drawLine(0, this.getHeight() / 2, this.getWidth(), this.getHeight() / 2);
            }
        }
    }
}
