package aurora.triss.work.nestiospace.enums;

public enum HealthMessages {

    HEALTHY ("Altitude is A-OK"),
    RECOVERING ("Sustained Low Earth Orbit Resumed"),
    UNHEALTHY ("WARNING: RAPID ORBITAL DECAY IMMINENT");

    private final String message;

    HealthMessages(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return message;
    }
}
