package aurora.triss.work.nestiospace;

import aurora.triss.work.nestiospace.Persistence.HistoricalData;
import aurora.triss.work.nestiospace.controllers.DefaultController;
import aurora.triss.work.nestiospace.controllers.SpaceController;
import aurora.triss.work.nestiospace.enums.HealthMessages;
import aurora.triss.work.nestiospace.interfaces.NestioSpaceService;
import aurora.triss.work.nestiospace.jsonobjects.StatsData;
import aurora.triss.work.nestiospace.services.NestioSpaceServiceImplementation;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class NestioSpaceApplicationTests {

	@Test
	void contextLoads() {
		String response = "";
		try {
			DefaultController defaultController = new DefaultController();
			response = defaultController.defaultContext();
		}
		catch (Exception e){
			fail(e.getMessage());
		}
		assertEquals(response, "The Nestio Space Monitoring Server is Online");
	}

	@Test
	void healthy() {
		String response = "";
		try {
			LocalDateTime now = LocalDateTime.now();
			LinkedList<HistoricalData> historicalDataList = new LinkedList();
			historicalDataList.add(new HistoricalData(160.0, now, now));

			NestioSpaceService spaceService = new NestioSpaceServiceImplementation();
			spaceService.setHistoricalData(historicalDataList);

			SpaceController spaceController = new SpaceController();
			spaceController.setSpaceService(spaceService);

			response = spaceController.health();
		}
		catch (Exception e){
			fail(e.getMessage());
		}
		assertEquals(response, HealthMessages.HEALTHY.toString());
	}

	@Test
	void recovering() {
		String response = "";
		try {
			LocalDateTime now = LocalDateTime.now();
			LinkedList<HistoricalData> historicalDataList = new LinkedList();
			historicalDataList.add(new HistoricalData(160.0, now, now));
			historicalDataList.add(new HistoricalData(155.0, now, now.minusSeconds(90)));

			NestioSpaceService spaceService = new NestioSpaceServiceImplementation();
			spaceService.setHistoricalData(historicalDataList);

			SpaceController spaceController = new SpaceController();
			spaceController.setSpaceService(spaceService);

			response = spaceController.health();
		}
		catch (Exception e){
			fail(e.getMessage());
		}
		assertEquals(response, HealthMessages.RECOVERING.toString());
	}

	@Test
	void unhealthy() {
		String response = "";
		try {
			LocalDateTime now = LocalDateTime.now();
			LinkedList<HistoricalData> historicalDataList = new LinkedList();
			historicalDataList.add(new HistoricalData(155.0, now, now));

			NestioSpaceService spaceService = new NestioSpaceServiceImplementation();
			spaceService.setHistoricalData(historicalDataList);

			SpaceController spaceController = new SpaceController();
			spaceController.setSpaceService(spaceService);

			response = spaceController.health();
		}
		catch (Exception e){
			fail(e.getMessage());
		}
		assertEquals(response, HealthMessages.UNHEALTHY.toString());
	}

	@Test
	void testStats() {
		StatsData statsData = null;
		try {
			LocalDateTime now = LocalDateTime.now();
			LinkedList<HistoricalData> historicalDataList = new LinkedList();
			historicalDataList.add(new HistoricalData(160.0, now, now));
			historicalDataList.add(new HistoricalData(155.0, now, now.minusSeconds(90)));
			historicalDataList.add(new HistoricalData(165.0, now, now.minusSeconds(90)));

			NestioSpaceService spaceService = new NestioSpaceServiceImplementation();
			spaceService.setHistoricalData(historicalDataList);

			SpaceController spaceController = new SpaceController();
			spaceController.setSpaceService(spaceService);

			String response = spaceController.stats();

			statsData = deserialize(response);
		}
		catch (Exception e){
			fail(e.getMessage());
		}
		assertNotNull(statsData);
		assertEquals(statsData.getMaxAltitude(), 165.0);
		assertEquals(statsData.getMinAltitude(), 155.0);
	}

	private static StatsData deserialize(String json) throws Exception{
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		StatsData statsData = objectMapper.readValue(json, StatsData.class);
		return statsData;
	}
}
