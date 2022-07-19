package aurora.triss.work.nestiospace.jsonobjects;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class SatelliteData {

    @JsonProperty("last_updated")
    private String lastUpdated;
    private String altitude;

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLast_updated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getAltitude() {
        return altitude;
    }

    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }
}
