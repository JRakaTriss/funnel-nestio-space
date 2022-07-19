package aurora.triss.work.nestiospace.controllers;

import aurora.triss.work.nestiospace.enums.HealthMessages;
import aurora.triss.work.nestiospace.interfaces.NestioSpaceService;
import aurora.triss.work.nestiospace.jsonobjects.StatsData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpaceController {

    @Autowired
    private NestioSpaceService spaceService;

    @GetMapping("/stats")
    public String stats(){
        StatsData statsData;
        ObjectMapper objectMapper = new ObjectMapper();
        String returnData = "There was a problem retrieving data";
        try {
            statsData = spaceService.getStatsData();
            returnData = objectMapper.writeValueAsString(statsData);
        }
        catch (Exception e) {
            System.out.println("Error Logged: " + e.toString());
            e.printStackTrace();
        }
        return returnData;
    }

    @GetMapping("/health")
    public String health(){
        HealthMessages healthData;
        ObjectMapper objectMapper = new ObjectMapper();
        String returnData = "There was a problem retrieving data";
        try {
            returnData = spaceService.getHealthData().toString();
        }
        catch (Exception e) {
            System.out.println("Error Logged: " + e.toString());
            e.printStackTrace();
        }
        return returnData;
    }

    public void setSpaceService(NestioSpaceService spaceService) {
        this.spaceService = spaceService;
    }
}
