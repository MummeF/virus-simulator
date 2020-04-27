package process.time;

import lombok.AllArgsConstructor;
import lombok.Data;

import static process.ConfigLib.ILLNESS_DURATION;
import static process.ConfigLib.INCUBATION_TIME;

@Data
@AllArgsConstructor
public class Timestamp {
    private long time;

    public boolean isOutOfTimeRange(Timestamp otherTimeStamp, long range){
        return Math.abs(otherTimeStamp.getTime() - time) > Math.abs(range);
    }

    public boolean isStillInfected(){
        return this.isOutOfTimeRange(TimeLine.getAktTimeStamp(), INCUBATION_TIME + ILLNESS_DURATION);
    }

    public boolean isIncubationTimeOver(){
        return this.isOutOfTimeRange(TimeLine.getAktTimeStamp(), INCUBATION_TIME);
    }

}
