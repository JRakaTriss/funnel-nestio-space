package aurora.triss.work.nestiospace.services;

import aurora.triss.work.nestiospace.Persistence.HistoricalData;
import aurora.triss.work.nestiospace.enums.HealthMessages;
import aurora.triss.work.nestiospace.interfaces.NestioSpaceService;
import aurora.triss.work.nestiospace.jsonobjects.SatelliteData;
import aurora.triss.work.nestiospace.jsonobjects.StatsData;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedList;

@Service
public class NestioSpaceServiceImplementation implements NestioSpaceService {

    private static LinkedList<HistoricalData> historicalData = new LinkedList();

    private static String satelliteDataUrl = "http://nestio.space/api/satellite/data";

    public void refreshSatelliteData(){

        HistoricalData newHistoricalData = new HistoricalData();
        LocalDateTime now = LocalDateTime.now();
        try{
            String getJson = getJson(satelliteDataUrl);
            SatelliteData satelliteData = deserialize(getJson);

            newHistoricalData.setAltitude(Double.parseDouble(satelliteData.getAltitude()));

            int year  = Integer.parseInt(satelliteData.getLastUpdated().substring( 0, 4));
            int month = Integer.parseInt(satelliteData.getLastUpdated().substring( 5, 7));
            int day   = Integer.parseInt(satelliteData.getLastUpdated().substring( 8,10));
            int hour  = Integer.parseInt(satelliteData.getLastUpdated().substring(11,13));
            int min   = Integer.parseInt(satelliteData.getLastUpdated().substring(14,16));
            int sec   = Integer.parseInt(satelliteData.getLastUpdated().substring(17,19));
            LocalDateTime tempDateTime = LocalDateTime.of(year,month,day,hour,min,sec);

            newHistoricalData.setStatusTime(tempDateTime);
            newHistoricalData.setSavedTime(now);
        }
        catch (Exception e) {
            System.out.println("Error Logged: " + e.toString());
            e.printStackTrace();
        }

        while(historicalData != null
                && historicalData.size() > 0
                && historicalData.getLast().getSavedTime().isBefore(now.minusMinutes(5))){
            historicalData.removeLast();
        }
        historicalData.push(newHistoricalData);
    }

    public StatsData getStatsData(){
        LocalDateTime now = LocalDateTime.now();
        Double min = null;
        Double max = null;
        if(historicalData.getFirst() != null) {
            min = historicalData.getFirst().getAltitude();
            max = historicalData.getFirst().getAltitude();

            int i = 1;
            while (i < historicalData.size()
                    && historicalData.get(i).getSavedTime().isAfter(now.minusMinutes(5))) {
                HistoricalData temp = historicalData.get(i);
                if (min > temp.getAltitude()){
                    min = temp.getAltitude();
                }
                if (max < temp.getAltitude()){
                    max = temp.getAltitude();
                }
                i++;
            }
        }

        StatsData statsData = new StatsData();

        statsData.setMinAltitude(min);
        statsData.setMaxAltitude(max);

        return statsData;
    }

    public HealthMessages getHealthData(){

        LocalDateTime now = LocalDateTime.now();
        Boolean healthCritical = false;
        Boolean healthRecovering = false;

        int i = 0;
        int j = 0;
        Double floatingValue = 0.0;
        LocalDateTime lastChecked;
        while(i < historicalData.size()
                && historicalData.get(i) != null
                && historicalData.get(i).getSavedTime().isAfter(now.minusMinutes(2))
                && !healthCritical && !healthRecovering){
            HistoricalData temp = historicalData.get(i);
            i++;

            floatingValue += temp.getAltitude();
            lastChecked = temp.getSavedTime();

            if(lastChecked.isBefore(now.minusMinutes(1))){
                floatingValue -= historicalData.get(j).getAltitude();
                j++;
                if(floatingValue/(i-j) < 160){
                    healthRecovering = true;
                    break;
                }
            }

            if(j == 0 && (i >= historicalData.size()
                || historicalData.get(i).getSavedTime().isBefore(now.minusMinutes(1)))){
                if((floatingValue / i) < 160.0){
                    healthCritical = true;
                    break;
                }
            }
        }

        if(healthCritical){
            return HealthMessages.UNHEALTHY;
        }

        if(healthRecovering){
            return HealthMessages.RECOVERING;
        }

        return HealthMessages.HEALTHY;
    }

    private static String getJson(String getUrlString) throws Exception{
        //configure url
        URL getUrl = new URL(getUrlString);
        HttpURLConnection getConnection = (HttpURLConnection) getUrl.openConnection();
        getConnection.setRequestMethod("GET");
        getConnection.setRequestProperty("Content-Type", "application/json");
        getConnection.setReadTimeout(5000);


        int status = getConnection.getResponseCode();
        if(status > 299){
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(getConnection.getErrorStream()));
            String errorLine;
            StringBuffer error = new StringBuffer();
            while((errorLine = errorReader.readLine()) != null){
                error.append(errorLine);
            }
            errorReader.close();
            throw new RuntimeException(error.toString());
        }

        BufferedReader getReader = new BufferedReader(new InputStreamReader(getConnection.getInputStream()));
        String getLine;
        StringBuffer getContent = new StringBuffer();
        while((getLine = getReader.readLine()) != null){
            getContent.append(getLine);
        }
        getReader.close();
        String json = getContent.toString();
        return json;
    }

    private static SatelliteData deserialize(String json) throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        SatelliteData rootStructure = objectMapper.readValue(json, SatelliteData.class);
        return rootStructure;
    }

    public LinkedList<HistoricalData> getHistoricalData() {
        return historicalData;
    }

    public void setHistoricalData(LinkedList<HistoricalData> historicalData) {
        NestioSpaceServiceImplementation.historicalData = historicalData;
    }
}
