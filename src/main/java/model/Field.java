package model;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public String toString() {
        String personString = "[" + this.persons.stream()
                .map(Person::toString)
                .collect(Collectors.joining(","))
                + "]";
        personString.replaceAll("\n", "\n\t");
        return String.format("Field [%d | %d] {\n\tpersons: %s\n}",
                this.x, this.y, personString);
    }
}
