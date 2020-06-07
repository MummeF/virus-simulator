package model;

import Gui.Map.GraphicPerson;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import process.time.TimeLine;
import process.time.Timestamp;

import java.awt.*;

import static process.ConfigLib.*;

@Data
public class Person {
    //Verwaltung
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
//    private static int ACT_IDENT_NO = Integer.MIN_VALUE;
    private static int ACT_IDENT_NO = 0;
    private int ident;

    //Infiziertenstatus

    private boolean alive = true;
    private boolean immune = false;
    private boolean infected = false;
    private double riskFactor = initRiskFactor();

    //move
    private Point recentPosition;

    private GraphicPerson graphicPerson;


    private Timestamp timeOfInfection;

    //Persönliche Daten
    private int age;
    private boolean male = Math.random() > 0.5;
    private String name = (male ? "John" : "Jane") + " Doe";


    public Person(int age) {
        this.ident = ACT_IDENT_NO;
        ACT_IDENT_NO++;
        this.age = age;
    }

    public Person() {
        this((int) (Math.random() * 80) + 10);
    }

    public String getSex() {
        return male ? "male" : "female";
    }

    public int getFieldsToMove() {
        return this.age > 40 ? 1 : 2;
    }

    public boolean riskGroup() {
        return age > RISK_AGE;
    }

    private double initRiskFactor() {
        return (riskGroup() ? LETHALITY_PLUS_FOR_RISK_AGE : 0) + LETHALITY;
    }

    public void infect() {
        if (!infected && !immune) {
            this.infected = true;
            this.timeOfInfection = TimeLine.getAktTimeStamp();
        }
    }

    public void process() {
        if (infected) {
            if (!timeOfInfection.isStillInfected()) {
                this.infected = false;
                this.immune = true;
            } else {
                if (!timeOfInfection.isIncubationTimeOver()) {
                    if (Math.random() < riskFactor) {
                        this.infected = false;
                        this.immune = false;
                        this.alive = false;
                        System.out.println(this.toString() + " ist gestorben!!!!!!!!!!!!!!!!!!!!!!!");
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        return String.format("Person %d{\n\tage: %d\n\tname: %s\n\tsex: %s\n\trisk group: %b\n\timmune: %b\n\tinfected: %b\n\tinfection time: %s\n}",
                this.ident, this.age, this.name, this.getSex(), this.riskGroup(), this.immune, this.infected, this.timeOfInfection);
    }

    public void updateGraphicPerson(int x, int y, int fieldWidth, int fieldHeight) {
        this.graphicPerson.update(this, x, y, fieldWidth, fieldHeight);
    }
}
