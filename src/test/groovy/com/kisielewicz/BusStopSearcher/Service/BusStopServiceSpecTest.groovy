package com.kisielewicz.BusStopSearcher.Service

import com.kisielewicz.BusStopSearcher.repository.BusStopRepository
import com.kisielewicz.BusStopSearcher.service.BusStopService
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

class BusStopServiceSpecTest extends Specification {

    private final BusStopRepository busStopRepository = Mock(BusStopRepository)
    private final RestTemplate restTemplate = Mock(RestTemplate)

    private final BusStopService busStopService = new BusStopService(busStopRepository, restTemplate)

    def "should do correct calls on populateDB"() {
        given:
        String inputText = new File("src/test/resources/busStops.txt").text
        when:
        busStopService.populateDB()
        then:
        1*restTemplate.getForObject(_, String.class) >> inputText
        3*busStopRepository.save(_)

    }
}
