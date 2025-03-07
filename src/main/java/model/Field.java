package model;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static process.ConfigLib.PROBABILITY_OF_INFECTION_ON_FIELD;

@Data
public class Field {
    private int x = -1;
    private int y = -1;
    private List<Person> persons;

    @Builder
    public Field(int x, int y){
        this.x = x;
        this.y = y;
        persons = new ArrayList<>();
    }

    public void addPerson(Person toAdd) {
        this.persons.add(toAdd);
    }

    public boolean removePersonByIdentifier(int ident) {
        int index = -1;
        for (int i = 0; i < persons.size(); i++) {
            if (persons.get(i).getIdent() == ident) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            return false;
        }
        persons.remove(index);
        return true;
    }

    public boolean removePerson(Person toRem) {
        return this.persons.remove(toRem);
    }

    public void process(){
        long count = this.persons.stream()
                .filter(Person::isInfected)
                .count();
        if(count > 0) {
            for (Person person : this.persons) {
                if(Math.random() < (PROBABILITY_OF_INFECTION_ON_FIELD * Math.sqrt(count))){
                    person.infect();
                }
            }
        }
        persons.forEach(Person::process);
    }

    public boolean anyInfectedPersonOnIt(){
        return this.persons.stream().anyMatch(Person::isInfected);
    }
    public boolean anyImmunePersonOnIt(){
        return this.persons.stream().anyMatch(Person::isImmune);
    }

    @Override
    public String toString() {
        String personString = "[" + this.persons.stream()
                .map(Person::toString)
                .collect(Collectors.joining(",\n"))
                + "]";
        personString.replaceAll("\n", "\n\t");
        return String.format("Field [%d | %d] {\n\tpersons: %s\n}",
                this.x, this.y, personString);
    }
}
