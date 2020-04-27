package process.time;

import static process.ConfigLib.TIME_SPEED_STEP;

public class TimeLine {
    private static long aktTime = Long.MIN_VALUE;

    public static Timestamp getAktTimeStamp(){
        return new Timestamp(aktTime);
    }

    public static void processTime(){
        aktTime += TIME_SPEED_STEP;
    }
}
