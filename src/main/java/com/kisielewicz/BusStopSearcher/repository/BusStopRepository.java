package com.kisielewicz.BusStopSearcher.repository;

import com.kisielewicz.BusStopSearcher.domain.BusStop;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusStopRepository extends CrudRepository<BusStop, Long> {
}
