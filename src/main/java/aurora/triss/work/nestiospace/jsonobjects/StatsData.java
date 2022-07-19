package aurora.triss.work.nestiospace.jsonobjects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StatsData {

    @JsonProperty("min_altitude")
    private Double minAltitude;

    @JsonProperty("max_altitude")
    private Double maxAltitude;

    public Double getMinAltitude() {
        return minAltitude;
    }

    public void setMinAltitude(Double minAltitude) {
        this.minAltitude = minAltitude;
    }

    public Double getMaxAltitude() {
        return maxAltitude;
    }

    public void setMaxAltitude(Double maxAltitude) {
        this.maxAltitude = maxAltitude;
    }
}
