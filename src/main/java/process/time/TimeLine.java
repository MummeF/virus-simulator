package process.time;

import lombok.Getter;
import process.Simulation;

import java.util.ArrayList;
import java.util.List;

import static process.ConfigLib.TIME_SPEED_STEP;

public class TimeLine {
    //    private static long aktTime = Long.MIN_VALUE;
    private static long aktTime = 0;
    @Getter
    private static List<TimeValue> deads = new ArrayList<>();
    @Getter
    private static List<TimeValue> infected = new ArrayList<>();
    @Getter
    private static List<TimeValue> immune = new ArrayList<>();

    public static Timestamp getAktTimeStamp() {
        return new Timestamp(aktTime);
    }

    public static void processTime() {
        deads.add(new TimeValue(aktTime, Simulation.getDeads().size()));
        infected.add(new TimeValue(aktTime, Simulation.getInfectedCount()));
        immune.add(new TimeValue(aktTime, Simulation.getImmuneCount()));
        aktTime += TIME_SPEED_STEP;
    }
}
