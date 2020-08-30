package process;

import Gui.Gui;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import model.Border;
import model.Field;
import model.Move;
import model.Person;
import process.time.TimeLine;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static process.ConfigLib.*;

@Data
public class Simulation {
    @Setter
    private static Gui gui;


    @Getter
    private static List<Field> fields;
    @Getter
    private static List<Person> deads;


    @Getter
    private static boolean paused = false;
    @Getter
    private static boolean finished = false;

    private static boolean initialized = false;

    @Getter
    private static AtomicBoolean systemRunning;

    @Getter
    private static List<Border> borders;

    public static void pause() {
        paused = true;
    }

    public static boolean borderSettingAllowed(){
        return (!systemRunning.get() && initialized);
    }

    public static void init() {
        TimeLine.resetTime();
        gui.resetSimulation();
        fields = generateFields(MAX_X, MAX_Y);
        deads = new ArrayList<>();
        borders = new ArrayList<>();
        generatePersonsOnField();
        plantVirus();
        systemRunning = new AtomicBoolean(false);
        paused = false;
        finished = false;
        initialized = true;
        SwingUtilities.invokeLater(() -> gui.updateSimulation());
    }

    public static void addBorders(List<Border> toAdd, int borderStep) {
        borders.removeIf(border -> border.getTime() == borderStep);
        borders.addAll(toAdd);
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

    private static int getNextMovableField(int x, int y, Person person) {
        if (checkXAndY(x, y)) {
            int direction = getNextDirection(x, y, person);
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
        return getFieldOn(x, y);
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

    private static int getNextDirection(int x, int y, Person person) {
        if (Math.random() >= directionChange) {
            if (checkAccessible(x, y, x + (person.getLastDirection() == 0 ? 1 : person.getLastDirection() == 1 ? -1 : 0),
                    y + (person.getLastDirection() == 2 ? 1 : person.getLastDirection() == 3 ? -1 : 0))) {
                return person.getLastDirection();
            }
        }
        Boolean[] tries = new Boolean[4];
        for (int i = 0; i < 4; i++) {
            tries[i] = false;
        }
        while (!tries[0] || !tries[1] || !tries[2] || !tries[3]) {
            int direction = (int) (Math.random() * 4);
            if (checkAccessible(x, y, x + (direction == 0 ? 1 : direction == 1 ? -1 : 0),
                    y + (direction == 2 ? 1 : direction == 3 ? -1 : 0))) {
                person.setLastDirection(direction);
                return direction;
            }
            tries[direction] = true;
        }
        return -1;
    }

    private static boolean checkAccessible(int fromX, int fromY, int toX, int toY) {
        return (toX >= 0 && toX < MAX_X && toY >= 0 && toY < MAX_Y) && moveAllowed(fromX, fromY, toX, toY);
    }

    private static boolean moveAllowed(int fromX, int fromY, int toX, int toY) {
        if(borders.isEmpty()){
            return true;
        }
        return !borders.stream().anyMatch(border -> border.blocks(fromX, fromY, toX, toY));
    }

    private static void moveAllPersons() {
        List<Move> moves = new ArrayList<>();
        fields
                .forEach(field ->
                        field.getPersons().forEach(person -> {
                            if (Math.random() < MOBILITY) {
                                int from = getFieldOn(field.getX(), field.getY());
                                int to = getNextMovableField(field.getX(), field.getY(), person);
                                moves.add(Move.builder()
                                        .person(person)
                                        .fromIndex(from)
                                        .toIndex(to)
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

    public static void start() {
        paused = false;
        systemRunning.set(true);
        new Thread(() -> {
            long systemTime = System.currentTimeMillis() - TIME_SPEED;
            while (systemRunning.get()) {
                if (paused) {
                    systemRunning.set(false);
                } else if (systemTime + TIME_SPEED <= System.currentTimeMillis()) {
                    systemTime = System.currentTimeMillis();
                    for (int i = 0; i < TIME_SPEED_STEP; i++) {
                        process();
                    }
                    try {
                        SwingUtilities.invokeAndWait(() -> gui.updateSimulation());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    if (getInfectedCount() == 0) {
                        systemRunning.set(false);
                        paused = false;
                        finished = true;
                        initialized = false;
                        gui.updateSimulation();
                    }
                }
            }
//                System.exit(0);
        }).start();
    }
}
