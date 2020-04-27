package process;

import lombok.Data;
import model.Field;
import model.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static process.ConfigLib.*;

@Data
public class Simulation {
    private List<Field> fields = generateFields(MAX_X, MAX_Y);
    private List<Person> deads = new ArrayList<>();

    public void generatePersonsOnField() {
        this.fields.forEach(field -> {
            while (Math.random() < 0.3) {
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
                .filter(field ->
                        field.getPersons().stream()
                                .anyMatch(person -> person.isInfected())
                )
                .forEach(field -> System.out.println(field.toString()));
    }

    public void plantVirus() {
        AtomicInteger planted = new AtomicInteger(0);
        while (planted.get() == 0) {
            this.fields.stream()
                    .flatMap(field -> field.getPersons().stream())
                    .forEach(person -> {
                        if (planted.get() == 0 && Math.random() < 0.01) {
                            person.setInfected(true);
                            person.setName("Lee Yu");
                            planted.set(1);
                            return;
                        }
                    });
        }
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

    public Field getNextMovableField(int x, int y) {
        if (checkXAndY(x, y)) {
            List<Integer> possibleDirections = new ArrayList<>();
            if (checkFieldAccessible(x++, y)) {
                possibleDirections.add(0);
            } else if (checkFieldAccessible(x, y++)) {
                possibleDirections.add(1);
            } else if (checkFieldAccessible(x--, y)) {
                possibleDirections.add(2);
            } else if (checkFieldAccessible(x, y--)) {
                possibleDirections.add(3);
            }
            if (possibleDirections.isEmpty()) {
                return getFieldOn(x, y);
            } else {
                int directionNo = (int) (Math.random() * possibleDirections.size());
                switch (possibleDirections.get(directionNo)) {
                    case 0:
                        return getFieldOn(x++, y);
                    case 1:
                        return getFieldOn(x, y++);
                    case 2:
                        return getFieldOn(x--, y);
                    case 3:
                        return getFieldOn(x, y--);
                }
            }
        }
        return null;
    }

    public boolean checkFieldAccessible(int x, int y) {
        if (checkXAndY(x, y)) {
            return getFieldOn(x, y).isAccessible();
        }
        return false;
    }

    public Field getFieldOn(int x, int y) {
        if (checkXAndY(x, y)) {
            return this.fields.stream()
                    .filter(field -> field.getX() == x && field.getY() == y)
                    .collect(Collectors.collectingAndThen(Collectors.toList(), list -> {
                        if (list.isEmpty() || list.size() > 1) {
                            return null;
                        }
                        return list.get(0);
                    }));
        }
        return null;
    }

    public boolean checkXAndY(int x, int y) {
        return (x >= 0 && x <= MAX_X && y >= 0 && y <= MAX_Y);
    }

    public void declineField(int x, int y) {
        if (checkXAndY(x, y)) {
            Field toDecline = getFieldOn(x, y);
            List<Person> toMove = toDecline.decline();
            toMove.forEach(person ->
                    getNextMovableField(x, y).addPerson(person)
            );
        }
    }

    public void declineField(int fromX, int fromY, int toX, int toY) {
        if (checkXAndY(fromX, fromY) && checkXAndY(toX, toY)) {
            for (int x = fromX; (fromX < toX ? x < toX : x > toX); x += fromX < toX ? 1 : -1) {
                for (int y = fromY; (fromY < toY ? y < toY : y > toY); y += fromY < toY ? 1 : -1)
                    declineField(x, y);
            }
        }
    }

    public void process() {
        fields.forEach(Field::process);
        fields.forEach(field -> {
            List<Person> deadsInProcess = field.getPersons().stream()
                    .filter(Person::isAlive)
                    .collect(Collectors.toList());
            deadsInProcess.forEach(person -> field.removePerson(person));
            deads.addAll(deadsInProcess);
        });
    }

    public void run() throws InterruptedException {
        boolean systemRunning = true;
        while (systemRunning) {
            //..........
            process();
            Thread.sleep(TIME_SPEED);
        }
        System.exit(0);
    }
}
