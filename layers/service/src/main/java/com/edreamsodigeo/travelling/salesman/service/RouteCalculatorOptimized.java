package com.edreamsodigeo.travelling.salesman.service;

import com.edreamsodigeo.travellingsalesman.model.Flight;
import com.edreamsodigeo.travellingsalesman.store.FlightStore;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Inject;
import java.util.*;

/**
 * Route finder based on the Dijkstra's algorithms.
 * Optimizations used:
 * 1. Custom object to store city data:
 *    Instead of using three separate maps to store the cheapest price, shortest duration, and previous city for each visited city, a custom object can be created to store all this information together in a single data structure.
 *    This improves readability, reduce memory usage and also reduce the number of map lookups.
 * 2. Avoid searching for flights in the loop:
 *    The previous implementation is searching for flights in the loop using a for-loop, which can become inefficient for large datasets.
 *    Instead, the flights can be preprocessed and stored in a Map, where the key is the origin city, and the value is a List of flights that originate from that city.
 *    This way, the flights can be accessed directly using the origin city, which will reduce the search time.
 * 3. Keep track of visited nodes and early exit:
 *    The previous implementation is using a PriorityQueue to store the cities to visit, but there is no mechanism to prevent revisiting the same city multiple times.
 */
public class RouteCalculatorOptimized implements RouteCalculator{

    @Getter
    @Setter
    private class CityNode {
        private String cheapestPreviousCity;
        private int cost;
        private int duration;

        private boolean visited;

        public CityNode() {
            this.cheapestPreviousCity = null;
            this.cost = Integer.MAX_VALUE;
            this.duration = Integer.MAX_VALUE;
        }
    }
    private final FlightStore flightStore;

    @Inject
    public RouteCalculatorOptimized(FlightStore flightStore) {
        this.flightStore = flightStore;
    }


    @Override
    public List<Flight> calculate(String originCity, String destinationCity) {

        List<Flight> flights = flightStore.getFlights();

        if(flights.isEmpty()) {
            return Collections.emptyList();
        }

        // Create a map to store the city data
        Map<String, CityNode> cityMap = new HashMap<>();
        cityMap.put(originCity, new CityNode());

        // Create a map to store the flights
        Map<String, List<Flight>> flightsAdjacencyList = buildFlightAdjacencyList(flights);

        // Create a priority queue to store the cities to visit
        Queue<String> citiesToVisit = new PriorityQueue<>((a, b) -> cityMap.get(a).cost - cityMap.get(b).cost);
        citiesToVisit.offer(originCity);

        // Apply Dijkstra's algorithm
        while (!citiesToVisit.isEmpty()) {
            String currentCity = citiesToVisit.poll();
            CityNode currentCityNode = cityMap.get(currentCity);

            if (currentCityNode.isVisited() || currentCity.equals(destinationCity)) {
                break;
            }

            for (Flight flight : flightsAdjacencyList.getOrDefault(currentCity, new ArrayList<>())) {
                processNode(currentCityNode, flight, cityMap, citiesToVisit, currentCity);
            }

            currentCityNode.setVisited(true);
        }

        // Build the cheapest route from the start to the end city
        return buildCheapestRoute(cityMap, originCity, destinationCity, flights);
    }

    private Map<String, List<Flight>> buildFlightAdjacencyList(List<Flight> flights) {
        Map<String, List<Flight>> graph = new HashMap<>();
        for (Flight flight : flights) {
            if (!graph.containsKey(flight.getOriginCity())) {
                graph.put(flight.getOriginCity(), new ArrayList<>());
            }
            graph.get(flight.getOriginCity()).add(flight);
        }

        return graph;
    }


    private static List<Flight> buildCheapestRoute(Map<String, CityNode> cityMap, String startCityName, String destinationCityName, List<Flight> flights) {
        List<Flight> cheapestRoute = new ArrayList<>();
        CityNode endCityNode = cityMap.get(destinationCityName);
        if (endCityNode != null) {
            String currentCity = destinationCityName;
            while (cityMap.containsKey(currentCity) && !currentCity.equals(startCityName)) {
                CityNode currentCityNode = cityMap.get(currentCity);
                String previousCity = currentCityNode.getCheapestPreviousCity();
                for (Flight flight : flights) {
                    if (flight.getOriginCity().equals(previousCity) && flight.getDestinationCity().equals(currentCity)) {
                        cheapestRoute.add(0, flight);
                        break;
                    }
                }
                currentCity = previousCity;
            }
        }

        return cheapestRoute;
    }

    private void processNode(CityNode currentCityNode, Flight flight, Map<String, CityNode> cityMap, Queue<String> citiesToVisit, String currentCity) {
        int currentPrice = currentCityNode.getCost() + flight.getPrice();
        int currentDuration = currentCityNode.getDuration() + flight.getDurationHours();
        CityNode neighbourCityNode = cityMap.getOrDefault(flight.getDestinationCity(), new CityNode());

        if(currentPrice < neighbourCityNode.getCost() || (currentPrice == neighbourCityNode.getCost() && currentDuration < neighbourCityNode.getDuration())) {
            neighbourCityNode.setCost(currentPrice);
            neighbourCityNode.setDuration(currentDuration);
            neighbourCityNode.setCheapestPreviousCity(currentCity);
            cityMap.put(flight.getDestinationCity(), neighbourCityNode);
            citiesToVisit.offer(flight.getDestinationCity());
        }
    }
}
