import lombok.Data;
import model.Field;
import model.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Data
public class Simulation {
    private static final int MAX_X = 30, MAX_Y = 30;
    private List<Field> fields = generateFields(MAX_X, MAX_Y);

    public void generatePersonsOnField() {
        this.fields.forEach(field -> {
            if (Math.random() < 0.3) {
                field.addPerson(new Person());
            }
        });
    }

    public void printAllFieldsWithPersonOnIt() {
        this.fields.stream()
                .filter(field -> field.getPersons().size() > 0)
                .forEach(field -> System.out.println(field.toString()));
    }
    public void printAllFieldsWithInfectedPersonOnIt() {
        this.fields.stream()
                .filter(field -> field.getPersons().size() > 0)
                .forEach(field -> System.out.println(field.toString()));
    }

    public void plantVirus(){
        AtomicInteger planted = new AtomicInteger();
        this.fields.stream()
                .flatMap(field->field.getPersons().stream())
                .forEach(person -> {
                    if(planted.get() == 0 && Math.random() < 0.1){
                        person.setInfected(true);
                        person.setName("Lee Yu");
                        planted.set(1);
                    }
                });
    }

    private ArrayList<Field> generateFields(int maxX, int maxY) {
        ArrayList<Field> generated = new ArrayList<>();
        for (int x = 0; x < maxX; x++) {
            for (int y = 0; y < maxY; y++) {
                generated.add(Field.builder()
                        .x(x)
                        .y(y)
                        .build());
            }
        }
        return generated;
    }

    public void startVirus() throws InterruptedException {
        //TODO:
        while (true){
            //..........

            Thread.sleep(3000);
        }
    }

    //Vorführung
//    private List<Person> persons = new ArrayList<>();
//
//    public void generateRandomPersons(int no) {
//        for (int i = 0; i < no; i++) {
//            this.persons.add(new Person());
//        }
//    }
//
//    public void printAllPersons() {
//        this.persons.stream()
//                .forEach(System.out::println);
//    }
//
//    public void printAllPersonsOver18() {
//        this.persons.stream()
//                .filter(person -> person.getAge() >= 18)
//                .forEach(System.out::println);
//    }
}
