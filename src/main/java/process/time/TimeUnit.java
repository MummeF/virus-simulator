package process.time;

import lombok.Getter;

public enum TimeUnit {
    Weeks("Wochen", "w"), Days("Tage", "d"), Hours("Stunden", "h");

    @Getter
    private final String longName;
    @Getter
    private final String shortName;


    TimeUnit(String longName, String shortName) {
        this.longName = longName;
        this.shortName = shortName;
    }

    public static TimeUnit ofLongName(String longName) {
       if(longName.equals(Weeks.longName)){
           return Weeks;
       }else if(longName.equals(Days.longName)){
           return Days;
       }else if(longName.equals(Hours.longName)){
           return Hours;
       }else {
           return null;
       }
    }
}
