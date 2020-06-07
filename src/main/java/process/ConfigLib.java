package process;

import process.time.TimeUnit;

import java.awt.*;

public class ConfigLib {

    //FIELD CONFIG
    public static int SIZE_FACTOR = 2;
    private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private static double screenFactor = screenSize.getWidth() / screenSize.getHeight();
    public static int MAX_X = SIZE_FACTOR, MAX_Y = (int) (SIZE_FACTOR * screenFactor);
    public static double MOBILITY = 0;
    public static double POPULATION_DENSITY = 0.3;

    //VIRUS CONFIG
    public static int RISK_AGE = 65;
    public static double LETHALITY = 0.05;
    public static double LETHALITY_PLUS_FOR_RISK_AGE = 0.1;
    public static double PROBABILITY_OF_INFECTION_ON_FIELD = 0.5;


    //TIME CONFIG
//    public static int TIME_SPEED = 1000 / 24; //24 Hz
    public static int TIME_SPEED = 2000;
    public static int TIME_SPEED_STEP = 1;
    public static TimeUnit TIME_UNIT = TimeUnit.Days;
    public static long INCUBATION_TIME = 10;
    public static long ILLNESS_DURATION = 5;

}
