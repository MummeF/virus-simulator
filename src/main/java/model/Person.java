package model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Data
public class Person {
    //Verwaltung
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
//    private static int ACT_IDENT_NO = Integer.MIN_VALUE;
    private static int ACT_IDENT_NO = 0;
    private int ident;

    //Infiziertenstatus
    private static int RISK_AGE = 65;

    private boolean alive = true;
    private boolean immune = false;
    private boolean infected = false;
    private Optional<Integer> timeTilHealthy;

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

    @Override
    public String toString() {
        return String.format("Person %d{\n\tage: %d\n\tname: %s\n\tsex: %s\n\trisk group: %b\n\timmune: %b\n\tinfected: %b\n}",
                this.ident, this.age, this.name, this.getSex(), this.riskGroup(), this.immune, this.infected);
    }
}
