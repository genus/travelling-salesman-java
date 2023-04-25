package com.edreamsodigeo.travellingsalesman.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Flight {
    private String originCity;
    private String destinationCity;
    private int price;
    private int durationHours;
}
