package com.kisielewicz.BusStopSearcher.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kisielewicz.BusStopSearcher.domain.BusStop;
import com.kisielewicz.BusStopSearcher.repository.BusStopRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;
import java.util.stream.StreamSupport;

@Service
public class BusStopService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BusStopService.class);

    @Value("${stops.url}")
    private static String URL;

    private BusStopRepository busStopRepository;
    private RestTemplate restTemplate;

    @Autowired
    public BusStopService(BusStopRepository busStopRepository, RestTemplate restTemplate) {
        this.busStopRepository = busStopRepository;
        this.restTemplate = restTemplate;
    }

    @Transactional
    public void populateDB() {
        LOGGER.info("Starting DB population");
        String result = restTemplate.getForObject(URL, String.class);
        LOGGER.info("Got data from URL: {}", result);

        try {
            LOGGER.info("Trying to parse data");
            JsonNode resultNode = new ObjectMapper().readTree(result);
            JsonNode node = resultNode.get("features");
            for (JsonNode objectNode : node) {
                BusStop busStop = new BusStop();
                JsonNode coordinates = objectNode.get("geometry").get("coordinates");
                busStop.setCoordinates(Arrays.asList(coordinates.get(0).doubleValue(),
                        coordinates.get(1).doubleValue()));
                busStop.setStopId(objectNode.get("id").textValue());
                busStop.setName(objectNode.get("properties").get("stop_name").textValue());
                busStopRepository.save(busStop);
                LOGGER.info("Parsed Bus Stop and saved to DB: {}", busStop);
            }
        } catch (JsonParseException e) {
            LOGGER.error("Problem with parsing data from URL", e);
        } catch (IOException e) {
            LOGGER.error("IOException of unknown nature while parsing");
        }
        LOGGER.info("Successfully parsed all data from URL");
    }

    public BusStop getClosest(Double latitude, Double longitude) {
        LOGGER.info("Getting closest bus stop for set latitude: {} and longitude: {}", latitude, longitude);
        Iterable<BusStop> busStops = busStopRepository.findAll();
        LOGGER.info("Got bus stops from database");

        LOGGER.info("Comparing distances from set lat and long in a stream");
        BusStop result =  StreamSupport.stream(busStops.spliterator(), false)
                .min(Comparator.comparingDouble(busStop -> getDistance(busStop, latitude, longitude)))
                .get();
        LOGGER.info("Found closest bus stop: {}", result);
        return result;
    }

    private Double getDistance(BusStop busStop, Double latitude, Double longitude) {
        LOGGER.info("Got bus stops from database");
        return Math.sqrt(Math.pow((latitude - busStop.getCoordinates().get(0)), 2) + Math.pow((longitude - busStop.getCoordinates().get(1)), 2));
    }
}
