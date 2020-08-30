package Gui.Settings.graph;

import lombok.Getter;
import process.ConfigLib;
import process.time.TimeValue;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GraphPanel extends JPanel {
    private List<TimeValue> deads;
    private List<TimeValue> infected;
    private List<TimeValue> immune;
    private int scalaMax;
    private JLabel scalaXMax;
    private boolean relative;
    private JScrollBar scrollBar;
    @Getter
    private static final int RELATIVE_NUMBER = 10;
    private int scrollPosition = 0;

    public GraphPanel(List<TimeValue> deads, List<TimeValue> infected, List<TimeValue> immune,
                      boolean relative, int width, int height) {
        this.setPreferredSize(new Dimension(width, height));
        this.relative = relative;
        this.setLayout(null);
        this.setVisible(true);
        scalaXMax = new JLabel("");
        scalaXMax.setBounds(1, 1, 30, 14);
        this.add(scalaXMax);
        this.update(deads, infected, immune);
        scrollBar = new JScrollBar(JScrollBar.HORIZONTAL, infected.size(), infected.size(), 0, infected.size());
        scrollBar.addAdjustmentListener(e -> {
            scrollPosition = e.getValue();
            repaint();
        });
        this.add(scrollBar);
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
        scrollBar.setBounds(0, this.getHeight() - 15, this.getWidth(), 15);
        scrollBar.setVisible(this.relative);
        this.paintScala(g2d);
        this.paintGraph(g2d);
    }

    private void paintGraph(Graphics2D g2d) {
        if (this.relative) {
            g2d.setColor(Color.BLACK);
            if (!deads.isEmpty()) {
                for (int i = 0; i < deads.size(); i++) {
                    int x = translateX(deads.get(i).getTime());
                    int width = (this.getWidth() - 30) / (RELATIVE_NUMBER * 3);
                    int y = translateY(deads.get(i).getValue());
                    int height = this.getHeight() - 20 - y;
                    g2d.fillRect(x, y, width, height);
                }
            }
            g2d.setColor(Color.GREEN);
            if (!immune.isEmpty()) {
                for (int i = 0; i < immune.size(); i++) {
                    int width = (this.getWidth() - 30) / (RELATIVE_NUMBER * 3);
                    int x = translateX(immune.get(i).getTime()) + width;
                    int y = translateY(immune.get(i).getValue());
                    int height = this.getHeight() - 20 - y;
                    g2d.fillRect(x, y, width, height);
                }
            }
            g2d.setColor(Color.RED);
            if (!infected.isEmpty()) {
                for (int i = 0; i < infected.size(); i++) {
                    int width = (this.getWidth() - 30) / (RELATIVE_NUMBER * 3);
                    int x = translateX(infected.get(i).getTime()) + (width * 2);
                    int y = translateY(infected.get(i).getValue());
                    int height = this.getHeight() - 20 - y;
                    g2d.fillRect(x, y, width, height);
                }
                g2d.setColor(Color.BLACK);
                int index = 0;
                if (RELATIVE_NUMBER > scrollPosition) {
                    index = infected.size() < RELATIVE_NUMBER ? infected.size() - 1 : RELATIVE_NUMBER;
                } else {
                    index = scrollPosition - 1;
                }
                g2d.drawString(infected.get(index).getTime() + " " + ConfigLib.TIME_UNIT.getShortName(),
                        this.getWidth() - 30, this.getHeight() - 22);
            }

        } else {
            g2d.setColor(Color.BLACK);
            if (!deads.isEmpty()) {
                for (int i = 1; i < deads.size(); i++) {
                    g2d.drawLine(translateX(deads.get(i - 1).getTime()),
                            translateY(deads.get(i - 1).getValue()),
                            translateX(deads.get(i).getTime()),
                            translateY(deads.get(i).getValue())
                    );
                }
                g2d.drawString(deads.get(deads.size() - 1).getValue() + "",
                        translateX(deads.get(deads.size() - 1).getTime()),
                        translateY(deads.get(deads.size() - 1).getValue())
                );
            }
            g2d.setColor(Color.GREEN);
            if (!immune.isEmpty()) {
                for (int i = 1; i < immune.size(); i++) {
                    g2d.drawLine(translateX(immune.get(i - 1).getTime()),
                            translateY(immune.get(i - 1).getValue()),
                            translateX(immune.get(i).getTime()),
                            translateY(immune.get(i).getValue())
                    );
                }
                g2d.drawString(immune.get(immune.size() - 1).getValue() + "",
                        translateX(immune.get(immune.size() - 1).getTime()),
                        translateY(immune.get(immune.size() - 1).getValue())
                );
            }
            g2d.setColor(Color.RED);
            if (!infected.isEmpty()) {
                for (int i = 1; i < infected.size(); i++) {
                    g2d.drawLine(translateX(infected.get(i - 1).getTime()),
                            translateY(infected.get(i - 1).getValue()),
                            translateX(infected.get(i).getTime()),
                            translateY(infected.get(i).getValue())
                    );
                }
                g2d.drawString(infected.get(infected.size() - 1).getValue() + "",
                        translateX(infected.get(infected.size() - 1).getTime()),
                        translateY(infected.get(infected.size() - 1).getValue())
                );
                g2d.setColor(Color.BLACK);
                g2d.drawString(infected.get(infected.size() - 1).getTime() + " " + ConfigLib.TIME_UNIT.getShortName(),
                        translateX(infected.get(infected.size() - 1).getTime()) - 5, this.getHeight() - 2);
            }
        }
    }

    private void paintScala(Graphics2D g2d) {
        g2d.setColor(Color.BLACK);
        g2d.drawLine(10, this.getHeight() - (this.relative ? 20 : 15), this.getWidth() - 10,
                this.getHeight() - (this.relative ? 20 : 15));
        g2d.drawLine(10, 15, 10, this.getHeight() - (this.relative ? 20 : 15));
    }

    private int translateX(long time) {
        int stepSize = (this.getWidth() - 30) / (this.relative ? RELATIVE_NUMBER : deads.size());
        int scrollStart = stepSize * scrollPosition;
        int scrollPos = this.relative ? (scrollStart >= this.getWidth() ? (scrollStart - (this.getWidth() - 50)) : 0) : 0;
        return (int) (10 + stepSize * time) - scrollPos;
    }

    private int translateY(int value) {
        return this.getHeight() - (this.relative ? 20 : 15) - (((this.getHeight() - (this.relative ? 45 : 25)) * value)
                / (scalaMax != 0 ? scalaMax : 1));
    }

    public void update(List<TimeValue> deads, List<TimeValue> infected, List<TimeValue> immune) {
        this.deads = deads;
        this.infected = infected;
        this.immune = immune;
        if (this.relative) {
            this.deads = new ArrayList<>();
            if (!deads.isEmpty()) {
                this.deads.add(deads.get(0));
                for (int i = 1; i < deads.size(); i++) {
                    this.deads.add(new TimeValue(deads.get(i).getTime(),
                            deads.get(i).getValue() - deads.get(i - 1).getValue()));
                }
            }
            this.infected = new ArrayList<>();
            if (!infected.isEmpty()) {
                this.infected.add(infected.get(0));
                for (int i = 1; i < infected.size(); i++) {
                    this.infected.add(new TimeValue(infected.get(i).getTime(),
                            (infected.get(i).getValue() - infected.get(i - 1).getValue())
                                    + (immune.get(i).getValue() - immune.get(i - 1).getValue())
                                    + (deads.get(i).getValue() - deads.get(i - 1).getValue())));
                }
            }
            this.immune = new ArrayList<>();
            if (!immune.isEmpty()) {
                this.immune.add(immune.get(0));
                for (int i = 1; i < immune.size(); i++) {
                    this.immune.add(new TimeValue(immune.get(i).getTime(),
                            immune.get(i).getValue() - immune.get(i - 1).getValue()));
                }
            }
        }
        List<TimeValue> allValues = new ArrayList<>();
        allValues.addAll(this.deads);
        allValues.addAll(this.infected);
        allValues.addAll(this.immune);
        allValues.stream()
                .mapToInt(timeValue -> timeValue.getValue())
                .max().ifPresentOrElse(scala -> this.scalaMax = scala, () -> scalaMax = 1);
        scalaXMax.setText(scalaMax + "");
        scalaXMax.setBounds(1, 1, 30, 14);
        if (scrollBar != null) {
            boolean fixedRight = scrollBar.getMaximum() == scrollBar.getValue();
            scrollBar.setMaximum(infected.size());
            if (fixedRight) {
                scrollBar.setValue(scrollBar.getMaximum());
            }
        }
        this.revalidate();
        this.repaint();
    }

}
