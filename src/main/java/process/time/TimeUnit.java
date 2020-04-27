package process.time;

public enum TimeUnit {
    Weeks("Weeks"), Days("Days"), Hours("Hours"), Minutes("Minutes");

    private String value;



    TimeUnit(String value){
        this.value = value;
    }
    public String value(){
        return this.value;
    }
}
