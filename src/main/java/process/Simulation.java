package process;

import lombok.Data;
import model.Field;
import model.Move;
import model.Person;
import process.time.TimeLine;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static process.ConfigLib.*;

@Data
public class Simulation {
//    private Gui gui;


    private List<Field> fields = generateFields(MAX_X, MAX_Y);
    private List<Person> deads = new ArrayList<>();

    public Simulation() {
//        this.gui = gui;
        this.generatePersonsOnField();
        this.plantVirus();
    }

    public int getInfectedCount(){
        AtomicInteger counter = new AtomicInteger(0);
        this.fields.stream()
                .forEach(field -> field.getPersons().stream()
                .filter(Person::isInfected)
                .forEach(person -> counter.getAndIncrement()));
        return counter.get();
    }

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
                            person.infect();
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

    public int getNextMovableField(int x, int y) {
        if (checkXAndY(x, y)) {
            List<Integer> possibleDirections = new ArrayList<>();
            if (checkFieldAccessible(x + 1, y)) {
                possibleDirections.add(0);
            }
            if (checkFieldAccessible(x, y + 1)) {
                possibleDirections.add(1);
            }
            if (checkFieldAccessible(x - 1, y)) {
                possibleDirections.add(2);
            }
            if (checkFieldAccessible(x, y - 1)) {
                possibleDirections.add(3);
            }
            if (possibleDirections.isEmpty()) {
                return getFieldOn(x, y);
            } else {
                int directionNo = (int) (Math.random() * possibleDirections.size());
                switch (possibleDirections.get(directionNo)) {
                    case 0:
                        return getFieldOn(x + 1, y);
                    case 1:
                        return getFieldOn(x, y + 1);
                    case 2:
                        return getFieldOn(x - 1, y);
                    case 3:
                        return getFieldOn(x, y - 1);
                }
            }
        }
        return -1;
    }

    public boolean checkFieldAccessible(int x, int y) {
        if (checkXAndY(x, y)) {
            return this.fields.get(getFieldOn(x, y)).isAccessible();
        }
        return false;
    }

    public int getFieldOn(int x, int y) {
        if (checkXAndY(x, y)) {
            for (int i = 0; i < this.fields.size(); i++) {
                Field field = this.fields.get(i);
                if (field.getX() == x && field.getY() == y) {
                    return i;
                }
            }
        }
        return -1;
    }

    public boolean checkXAndY(int x, int y) {
        return (x >= 0 && x < MAX_X && y >= 0 && y < MAX_Y);
    }

    public void declineField(int x, int y) {
        if (checkXAndY(x, y)) {
            List<Person> toMove = this.fields.get(getFieldOn(x, y)).decline();
            toMove.forEach(person ->
                    this.fields.get(getNextMovableField(x, y)).addPerson(person)
            );
        }
    }

    public void allowField(int x, int y) {
        if (checkXAndY(x, y)) {
            this.fields.get(getFieldOn(x, y)).allow();
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

    private void moveAllPersons() {
        List<Move> moves = new ArrayList<>();
        this.fields
                .forEach(field ->
                        field.getPersons().forEach(person -> {
                            if (Math.random() < MOVABILITY) {
                                moves.add(Move.builder()
                                        .person(person)
                                        .fromIndex(getFieldOn(field.getX(), field.getY()))
                                        .toIndex(getNextMovableField(field.getX(), field.getY()))
                                        .build());
                            }
                        })
                );
        moves.forEach(move -> {
            this.fields.get(move.getFromIndex()).removePerson(move.getPerson());
            this.fields.get(move.getToIndex()).addPerson(move.getPerson());
        });
    }

    public void process() {
        this.moveAllPersons();
        TimeLine.processTime();
        fields.forEach(Field::process);
        fields.forEach(field -> {
            List<Person> deadsInProcess = field.getPersons().stream()
                    .filter(person -> !person.isAlive())
                    .collect(Collectors.toList());
            deadsInProcess.forEach(person -> field.removePerson(person));
            deads.addAll(deadsInProcess);
        });
    }

    public void run() {
        boolean systemRunning = true;
//        if (this.gui != null) {
//            gui.updateSimulation();
//        }
        printAllFieldsWithInfectedPersonOnIt();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (systemRunning) {
                    //..........
                    try {
                        Thread.sleep(TIME_SPEED);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    process();
            printAllFieldsWithInfectedPersonOnIt();
                    System.out.println(TimeLine.getAktTimeStamp());
//                    if (gui != null) {
////                        gui.updateSimulation();
////                    }
                }
                System.exit(0);
            }
        }).start();



    }
}
