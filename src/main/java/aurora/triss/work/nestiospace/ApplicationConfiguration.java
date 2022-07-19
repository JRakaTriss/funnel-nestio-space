package aurora.triss.work.nestiospace;

import aurora.triss.work.nestiospace.interfaces.NestioSpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class ApplicationConfiguration {

    @Autowired
    private NestioSpaceService spaceService;

    @Scheduled(cron = "*/5 * * * * *")
    public void checkHealthCron() {
        spaceService.refreshSatelliteData();
    }
}
