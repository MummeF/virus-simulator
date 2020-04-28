package process;

import Gui.MapPanel;
import lombok.Data;
import lombok.Getter;
import model.Field;
import model.Move;
import model.Person;
import process.time.TimeLine;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static process.ConfigLib.*;

@Data
public class Simulation {
    //    private Gui gui;
    private static MapPanel mapPanel;


    @Getter
    private static List<Field> fields = generateFields(MAX_X, MAX_Y);
    @Getter
    private static List<Person> deads = new ArrayList<>();

    public Simulation() {
//        this.gui = gui;
        this.generatePersonsOnField();
        this.plantVirus();
    }

    public static void init(MapPanel panel) {
        mapPanel = panel;
        generatePersonsOnField();
        plantVirus();
    }

    public static int getInfectedCount() {
        AtomicInteger counter = new AtomicInteger(0);
        fields.stream()
                .forEach(field -> field.getPersons().stream()
                        .filter(Person::isInfected)
                        .forEach(person -> counter.getAndIncrement()));
        return counter.get();
    }

    public static int getImmuneCount() {
        AtomicInteger counter = new AtomicInteger(0);
        fields.stream()
                .forEach(field -> field.getPersons().stream()
                        .filter(Person::isImmune)
                        .forEach(person -> counter.getAndIncrement()));
        return counter.get();
    }

    private static void generatePersonsOnField() {
        fields.forEach(field -> {
            while (Math.random() < POPULATION_DENSITY) {
                field.addPerson(new Person());
            }
        });
    }

    public static void printAllFieldsWithPersonOnIt() {
        fields.stream()
                .filter(field -> field.getPersons().size() > 0)
                .forEach(field -> System.out.println(field.toString()));
    }

    public static void printAllFieldsWithInfectedPersonOnIt() {
        fields.stream()
                .filter(field -> field.getPersons().size() > 0)
                .filter(field ->
                        field.getPersons().stream()
                                .anyMatch(person -> person.isInfected())
                )
                .forEach(field -> System.out.println(field.toString()));
    }

    private static void plantVirus() {
        AtomicInteger planted = new AtomicInteger(0);
        while (planted.get() == 0) {
            fields.stream()
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

    private static ArrayList<Field> generateFields(int maxX, int maxY) {
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

    private static int getNextMovableField(int x, int y) {
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

    private static boolean checkFieldAccessible(int x, int y) {
        if (checkXAndY(x, y)) {
            return fields.get(getFieldOn(x, y)).isAccessible();
        }
        return false;
    }

    private static int getFieldOn(int x, int y) {
        if (checkXAndY(x, y)) {
            for (int i = 0; i < fields.size(); i++) {
                Field field = fields.get(i);
                if (field.getX() == x && field.getY() == y) {
                    return i;
                }
            }
        }
        return -1;
    }

    private static boolean checkXAndY(int x, int y) {
        return (x >= 0 && x < MAX_X && y >= 0 && y < MAX_Y);
    }

    public static void declineField(int x, int y) {
        if (checkXAndY(x, y)) {
            List<Person> toMove = fields.get(getFieldOn(x, y)).decline();
            toMove.forEach(person ->
                    fields.get(getNextMovableField(x, y)).addPerson(person)
            );
        }
    }

    public static void allowField(int x, int y) {
        if (checkXAndY(x, y)) {
            fields.get(getFieldOn(x, y)).allow();
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

    private static void moveAllPersons() {
        List<Move> moves = new ArrayList<>();
        fields
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
            fields.get(move.getFromIndex()).removePerson(move.getPerson());
            fields.get(move.getToIndex()).addPerson(move.getPerson());
        });
    }

    private static void process() {
        moveAllPersons();
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

    public static void run() {
        AtomicBoolean systemRunning = new AtomicBoolean(true);
        printAllFieldsWithInfectedPersonOnIt();
        new Thread(() -> {
            synchronized (mapPanel) {
                while (systemRunning.get()) {
                    try {
                        Thread.sleep(TIME_SPEED);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    process();
                    mapPanel.updateSimulation();
                    printAllFieldsWithInfectedPersonOnIt();
                    System.out.println(TimeLine.getAktTimeStamp());
                    if (getInfectedCount() == 0) {
                        systemRunning.set(false);
                        System.out.println("VIRUS BESIEGT!! Anzahl an Toten: " + getDeads().size()
                                + ", Anzahl an Genesenen: " + getImmuneCount());
                    }
                }
                System.exit(0);
            }
        }).start();
    }
}
