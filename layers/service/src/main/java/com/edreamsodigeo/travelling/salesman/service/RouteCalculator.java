package com.edreamsodigeo.travelling.salesman.service;

import com.edreamsodigeo.travellingsalesman.model.Flight;

import java.util.List;

public interface RouteCalculator {
    /**
     * Find the cheapest and shortest route(s) from origin city to destination city.
     * @param originCity Origin city name
     * @param destinationCity Destination city name
     * @return List of {@link Flight} objects representing the cheapest and shortest route(s).
     */
    public List<Flight> calculate(String originCity, String destinationCity);
}
