package process;

import Gui.Map.MapPanel;
import lombok.Data;
import lombok.Getter;
import model.Field;
import model.Move;
import model.Person;
import process.time.TimeLine;

import java.awt.*;
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
            int direction = getNextDirection(x, y);
            switch (direction) {
                case 0:
                    return getFieldOn(x + 1, y);
                case 1:
                    return getFieldOn(x - 1, y);
                case 2:
                    return getFieldOn(x, y + 1);
                case 3:
                    return getFieldOn(x, y - 1);
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

    public static void declineField(int fromX, int fromY, int toX, int toY) {
        if (checkXAndY(fromX, fromY) && checkXAndY(toX, toY)) {
            for (int x = fromX; (fromX < toX ? x < toX : x > toX); x += fromX < toX ? 1 : -1) {
                for (int y = fromY; (fromY < toY ? y < toY : y > toY); y += fromY < toY ? 1 : -1) {
                    declineField(x, y);
                }
            }
        }
    }

    private static int getNextDirection(int x, int y) {
        Boolean[] tries = new Boolean[4];
        for (int i = 0; i < 4; i++) {
            tries[i] = false;
        }
        while (!tries[0] || !tries[1] || !tries[2] || !tries[3]) {
            int direction = (int) (Math.random() * 4);
            if (checkAccessible(x + (direction == 0 ? 1 : direction == 1 ? -1 : 0),
                    y + (direction == 2 ? 1 : direction == 3 ? -1 : 0))) {
                return direction;
            }
            tries[direction] = true;
        }
        return -1;
    }

    private static boolean checkAccessible(int x, int y) {
        return (x >= 0 && x < MAX_X && y >= 0 && y < MAX_Y) && fields.get(getFieldOn(x, y)).isAccessible();
    }

    private static void moveAllPersons() {
        List<Move> moves = new ArrayList<>();
        fields
                .forEach(field ->
                        field.getPersons().forEach(person -> {
                            if (Math.random() < MOBILITY) {
                                int from = getFieldOn(field.getX(), field.getY());
                                int to = getNextMovableField(field.getX(), field.getY());
                                moves.add(Move.builder()
                                        .person(person)
                                        .fromIndex(from)
                                        .toIndex(to)
                                        .from(new Point(fields.get(from).getX(),fields.get(from).getY()))
                                        .to(new Point(fields.get(to).getX(),fields.get(to).getY()))
                                        .build());
                            }
                        })
                );
        moves.forEach(move -> {
            move.getPerson().setRecentMove(move);
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
            long systemTime = System.currentTimeMillis() - TIME_SPEED;
            while (systemRunning.get()) {
                if (systemTime + TIME_SPEED <= System.currentTimeMillis()) {
                    systemTime = System.currentTimeMillis();
                    process();
//                    try {
//                        SwingUtilities.invokeAndWait(()->mapPanel.updateSimulation());
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    } catch (InvocationTargetException e) {
//                        e.printStackTrace();
//                    }
                    new Thread(() -> mapPanel.updateSimulation())
                            .start();
//                    printAllFieldsWithInfectedPersonOnIt();
                    System.out.println(TimeLine.getAktTimeStamp());
                    if (getInfectedCount() == 0) {
                        systemRunning.set(false);
                        System.out.println("VIRUS BESIEGT!! Anzahl an Toten: " + getDeads().size()
                                + ", Anzahl an Genesenen: " + getImmuneCount());
                    }
                }
            }
//                System.exit(0);
        }).start();
    }
}
