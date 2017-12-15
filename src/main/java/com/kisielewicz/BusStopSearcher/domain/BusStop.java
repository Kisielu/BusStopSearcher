package com.kisielewicz.BusStopSearcher.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class BusStop {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    @Column
    private String stopId;
    @Column
    private String name;
    @ElementCollection
    private List<Double> coordinates;
}
