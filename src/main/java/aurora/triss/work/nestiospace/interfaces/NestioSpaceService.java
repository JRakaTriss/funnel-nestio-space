package aurora.triss.work.nestiospace.interfaces;

import aurora.triss.work.nestiospace.Persistence.HistoricalData;
import aurora.triss.work.nestiospace.enums.HealthMessages;
import aurora.triss.work.nestiospace.jsonobjects.StatsData;
import aurora.triss.work.nestiospace.services.NestioSpaceServiceImplementation;

import java.util.LinkedList;

public interface NestioSpaceService {
    public void refreshSatelliteData();
    public StatsData getStatsData();
    public HealthMessages getHealthData();
    public LinkedList<HistoricalData> getHistoricalData();
    public void setHistoricalData(LinkedList<HistoricalData> historicalData);
}
