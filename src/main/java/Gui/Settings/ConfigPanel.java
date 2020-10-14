package Gui.Settings;

import process.Simulation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class ConfigPanel extends JPanel {
    private JButton startStopButton, resetButton;
    private JLabel field, virus, system, borderHint;
    private SingleInputPanel size, mobility, direction, population, riskAge, lethality, lethalityPlusForRiskage,
            incubationTime, illnessTime, infection, animatedMove, timeSpeed, timeSpeedStep, timeUnit;

    public ConfigPanel(int width) {
        this.setPreferredSize(new Dimension(width, 500));
        resetButton = new JButton("Zurücksetzen");
        resetButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Simulation.init();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        startStopButton = new JButton("Simulation starten");
        startStopButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (Simulation.getSystemRunning().get() || Simulation.isPaused()) {
                    if (Simulation.isPaused()) {
                        Simulation.start();
                    } else {
                        Simulation.pause();
                        updateSettings();
                    }
                } else {
                    if (Simulation.isFinished()) {
                        Simulation.init();
                    } else {
                        Simulation.start();
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        ToolTipManager.sharedInstance().setInitialDelay(100);
        ToolTipManager.sharedInstance().setDismissDelay(60000);
        this.addComponent(startStopButton, width / 2, 20);
        this.addComponent(resetButton, width / 4, 20);
        resetButton.setVisible(false);
        borderHint = new JLabel("Für Barrieren mit der Maus auf dem Feld klicken und ziehen.", SwingConstants.LEFT);
        borderHint.setFont(new Font(borderHint.getFont().getName(), Font.ITALIC, borderHint.getFont().getSize()));
        this.addComponent(borderHint, width, 20);
        field = new JLabel("Bewegung und Bevölkerung", SwingConstants.LEFT);
        field.setFont(new Font(field.getFont().getName(), Font.BOLD, field.getFont().getSize()));
        this.addComponent(field, width, 20);
        this.size = new SingleInputPanel(width, 30, SingleInputPanel.SIZE, "Größenfaktor:");
        size.setToolTipText("Der Faktor, der die Größe der Felder bestimmt. Dieser Wert bestimmt die vertikale Anzahl an Felder. Die horizontale Anzahl wird anhand der Bildschirmrelationen berechnet.");
        this.add(size);
        this.population = new SingleInputPanel(width, 30, SingleInputPanel.POPULATION, "Bevölkerungsdichte pro Feld:");
        population.setToolTipText("Dieser Wert bestimmt die Wahrscheinlichkeit, dass eine Person auf einem Feld spawnt. Ist eine Person gespawnt, so wird erneut gewürfelt, bis keine Person mehr spawnt.");
        this.add(population);
        this.mobility = new SingleInputPanel(width, 30, SingleInputPanel.MOBILITY, "Mobilitätsfaktor:");
        mobility.setToolTipText("Dies ist die Wahrscheinlichkeit, dass sich eine Person in einem Zug bewegt.");
        this.add(mobility);
        this.direction = new SingleInputPanel(width, 30, SingleInputPanel.DIRECTION, "Wahrscheinlichkeit zum Richtungswechsel:");
        direction.setToolTipText("Dies ist die Wahrscheinlichkeit, dass eine Person sich bewegt, wenn sie die Richtung wechselt.");
        this.add(direction);
        virus = new JLabel("Krankheit", SwingConstants.LEFT);
        virus.setFont(new Font(virus.getFont().getName(), Font.BOLD, virus.getFont().getSize()));
        this.addComponent(virus, width, 20);
        this.riskAge = new SingleInputPanel(width, 30, SingleInputPanel.RISKAGE, "Risikoalter:");
        riskAge.setToolTipText("Orientiert an Covid-19 simuliert dieser Simulator mit einem Risikoalter, ab dem die Personen anfälliger für das Virus sind.");
        this.add(riskAge);
        this.incubationTime = new SingleInputPanel(width, 30, SingleInputPanel.INCUBATION, "Inkubationszeit:");
        incubationTime.setToolTipText("Die Inkubationszeit der Krankheit in Zeiteinheiten.");
        this.add(incubationTime);
        this.illnessTime = new SingleInputPanel(width, 30, SingleInputPanel.ILLNESS, "Krankheitsdauer:");
        illnessTime.setToolTipText("Die Krankheitsdauer in Zeiteinheiten.");
        this.add(illnessTime);
        this.lethality = new SingleInputPanel(width, 30, SingleInputPanel.LETHALITY, "Tödlichkeitsfaktor pro Zeiteinheit:");
        lethality.setToolTipText("Dies ist die Wahrscheinlichkeit, mit der eine infizierte Person außerhalb der Inkubationszeit während einer Zeiteinheit stirbt.");
        this.add(lethality);
        this.lethalityPlusForRiskage = new SingleInputPanel(width, 30, SingleInputPanel.LETHALITYPLUSFORRISKAGE, "Erhöhter Tödlichkeitsfaktor für Risikogruppen:");
        lethalityPlusForRiskage.setToolTipText("Dies ist der Tödlichkeitsfaktor, der für Risikogruppen zu dem normalen Tödlichkeitsfaktor hinzugerechnet wird.");
        this.add(lethalityPlusForRiskage);
        this.infection = new SingleInputPanel(width, 30, SingleInputPanel.INFECTIONONFIELD, "Wahrscheinlichkeit der Infektion bei Kontakt:");
        infection.setToolTipText("Wahrscheinlichkeit, mit der eine Person sich bei Kontakt mit einem Infizierten selber infiziert. Kontakt ist dann hergestellt, wenn sich zwei Personen auf dem gleichen Feld befinden. Sind mehrere Infizierte auf dem Feld, erhöht sich die Wahrscheinlichkeit für gesunde Personen, sich zu infizieren (Formel: Wahrscheinlichkeit * Wurzel(Anzahl Infizierter))");
        this.add(infection);
        system = new JLabel("System", SwingConstants.LEFT);
        system.setFont(new Font(system.getFont().getName(), Font.BOLD, system.getFont().getSize()));
        this.addComponent(system, width, 20);
        this.animatedMove = new SingleInputPanel(width, 30, SingleInputPanel.ANIMATION, "Animierte Bewegung:");
        animatedMove.setToolTipText("Schaltet die Animierte Bewegung ein / aus. VORSICHT: Animierte Bewegung ist sehr rechenintensiv. Bei Lags / Bugs wird empfohlen, diese auszuschalten.");
        this.add(animatedMove);
        this.timeSpeed = new SingleInputPanel(width, 30, SingleInputPanel.TIMESPEED, "Dauer der Züge:");
        timeSpeed.setToolTipText("Die Dauer eines Zuges in ms.");
        this.add(timeSpeed);
        this.timeSpeedStep = new SingleInputPanel(width, 30, SingleInputPanel.TIMESPEEDSTEP, "Vergangene Zeiteinheit pro Zug:");
        this.add(timeSpeedStep);
        this.timeUnit = new SingleInputPanel(width, 30, SingleInputPanel.TIMEUNIT, "Zeiteinheit:");
        this.add(timeUnit);

    }

    public void updateSimulation() {
        updateSettings();
    }

    private void updateSettings() {
        if (Simulation.getSystemRunning().get() || Simulation.isPaused()) {
            if (Simulation.isPaused()) {
                startStopButton.setText("Start");
                startStopButton.setVisible(true);
                startStopButton.setPreferredSize(new Dimension(this.getWidth() / 4, 20));
                resetButton.setVisible(true);
                borderHint.setVisible(true);
            } else {
                startStopButton.setText("Pause");
                startStopButton.setVisible(true);
                startStopButton.setPreferredSize(new Dimension(this.getWidth() / 2, 20));
                resetButton.setVisible(false);
                borderHint.setVisible(false);
            }
            startStopButton.setVisible(true);
            size.setVisible(false);
            mobility.setVisible(true);
            population.setVisible(false);
            riskAge.setVisible(true);
            incubationTime.setVisible(true);
            illnessTime.setVisible(true);
            lethality.setVisible(true);
            lethalityPlusForRiskage.setVisible(true);
            infection.setVisible(true);
            animatedMove.setVisible(true);
            infection.setVisible(true);
            timeSpeed.setVisible(true);
            timeSpeedStep.setVisible(true);
            timeUnit.setVisible(false);
        } else {
            startStopButton.setVisible(true);
            startStopButton.setPreferredSize(new Dimension(this.getWidth() / 2, 20));
            resetButton.setVisible(false);
            borderHint.setVisible(true);
            if (Simulation.isFinished()) {

                borderHint.setVisible(false);
                startStopButton.setText("Simulation zurücksetzen");
                startStopButton.setVisible(true);
                size.setVisible(false);
                mobility.setVisible(true);
                population.setVisible(false);
                riskAge.setVisible(true);
                incubationTime.setVisible(true);
                illnessTime.setVisible(true);
                lethality.setVisible(true);
                lethalityPlusForRiskage.setVisible(true);
                infection.setVisible(true);
                animatedMove.setVisible(true);
                infection.setVisible(true);
                timeSpeed.setVisible(true);
                timeSpeedStep.setVisible(true);
                timeUnit.setVisible(false);
            } else {
                startStopButton.setText("Simulation starten");
                size.setVisible(true);
                mobility.setVisible(true);
                population.setVisible(true);
                riskAge.setVisible(true);
                incubationTime.setVisible(true);
                illnessTime.setVisible(true);
                lethality.setVisible(true);
                lethalityPlusForRiskage.setVisible(true);
                infection.setVisible(true);
                animatedMove.setVisible(true);
                infection.setVisible(true);
                timeSpeed.setVisible(true);
                timeSpeedStep.setVisible(true);
                timeUnit.setVisible(true);
            }
        }
    }

    private void addComponent(Component component, int width, int height) {
        component.setPreferredSize(new Dimension(width, height));
        this.add(component);
    }
}
