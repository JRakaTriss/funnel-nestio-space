package aurora.triss.work.nestiospace.Persistence;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class HistoricalData {
    private Double altitude;
    private LocalDateTime statusTime;
    private LocalDateTime savedTime;

    public HistoricalData(){
    }

    public HistoricalData(Double altitude, LocalDateTime statusTime, LocalDateTime savedTime) {
        this.altitude = altitude;
        this.statusTime = statusTime;
        this.savedTime = savedTime;
    }
    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }

    public Double getAltitude() {
        return altitude;
    }

    public void setSavedTime(LocalDateTime savedTime) {
        this.savedTime = savedTime;
    }

    public LocalDateTime getSavedTime() {
        return savedTime;
    }

    public void setStatusTime(LocalDateTime statusTime) {
        this.statusTime = statusTime;
    }

    public LocalDateTime getStatusTime() {
        return statusTime;
    }
}
