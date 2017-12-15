package com.kisielewicz.BusStopSearcher.web;

import com.kisielewicz.BusStopSearcher.domain.BusStop;
import com.kisielewicz.BusStopSearcher.service.BusStopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stops")
public class BusStopController {

    private BusStopService busStopService;

    @Autowired
    public BusStopController(BusStopService busStopService) {
        this.busStopService = busStopService;
    }

    @GetMapping("/populate")
    public void getDataFromWebsiteAndPutIntoDB() {
        busStopService.populateDB();
    }

    @GetMapping("/closest")
    public @ResponseBody BusStop getClosestBusStop(@RequestParam("latitude") Double latitude, @RequestParam("longitude") Double longitude) {
        return busStopService.getClosest(latitude, longitude);
    }
}
