package com.edreamsodigeo.travelling.salesman.service;

import com.edreamsodigeo.travellingsalesman.model.Flight;
import com.edreamsodigeo.travellingsalesman.store.FlightStore;

import javax.inject.Inject;
import java.util.*;

/**
 * Route finder based on the Dijkstra's algorithms, which is a well-known algorithm for finding the shortest path between nodes in a graph. In this case, each city will be represented as a node in the graph, and the flights will be represented as edges between nodes. We will use the flight price as the weight of each edge, and the flight duration as an additional property to take into account when comparing paths.
 * The algorithm will work as follows:
 *     Create a set of unvisited nodes, initially containing all cities.
 *     Create a map of the cheapest prices found so far for each node, initially set to infinity for all nodes except the starting node, which is set to 0.
 *     Create a map of the shortest durations found so far for each node, initially set to infinity for all nodes except the starting node, which is set to 0.
 *     Create a map of the cheapest flights found so far for each node, initially set to null for all nodes.
 *     While the visit cities queue is not empty:
 *         Select the unvisited node with the cheapest price found so far.
 *         For each of its neighboring nodes (i.e., cities that can be reached by a direct flight):
 *             Calculate the total price of the flight (i.e., the price of the current flight plus the cheapest price found so far for the current node).
 *             Calculate the total duration of the flight (i.e., the duration of the current flight plus the shortest duration found so far for the current node).
 *             If the total price is cheaper than the cheapest price found so far for the neighboring node, update the cheapest price, the shortest duration, and the cheapest route found so far for the neighboring node.
 *     Return the cheapest route found for the destination city.
 */
public class RouteCalculatorImpl implements RouteCalculator {

    private final FlightStore flightStore;

    @Inject
    public RouteCalculatorImpl(FlightStore flightStore) {
        this.flightStore = flightStore;
    }

    @Override
    public List<Flight> calculate(String originCity, String destinationCity) {

        List<Flight> flights = flightStore.getFlights();

        if(flights.isEmpty()) {
            return Collections.emptyList();
        }

        // Create a map to store the cities and their corresponding cheapest price and shortest duration
        Map<String, Integer> cheapestPrice = new HashMap<>();
        Map<String, Integer> shortestDuration = new HashMap<>();

        // Create a map to store the previous city in the cheapest route
        Map<String, String> cheapestFlights = new HashMap<>();

        // Initialize the maps with the start city
        cheapestPrice.put(originCity, 0);
        shortestDuration.put(originCity, 0);

        // Create a priority queue to store the cities to visit
        PriorityQueue<String> citiesToVisit = new PriorityQueue<>(Comparator.comparingInt(cheapestPrice::get));

        // Add the start city to the queue
        citiesToVisit.offer(originCity);

        // Apply Dijkstra's algorithm
        while (!citiesToVisit.isEmpty()) {
            String currentCity = citiesToVisit.poll();

            // Check if we have reached the end city
            if (currentCity.equals(destinationCity)) {
                break;
            }

            // Find the cheapest price and shortest duration to reach the neighbors of the current city
            for (Flight flight : flights) {
                if (flight.getOriginCity().equals(currentCity)) {
                    int currentPrice = cheapestPrice.get(currentCity) + flight.getPrice();
                    int currentDuration = shortestDuration.get(currentCity) + flight.getDurationHours();
                    String neighborCity = flight.getDestinationCity();

                    // Check if this is the cheapest and shortest route to the neighbor city
                    if (!cheapestPrice.containsKey(neighborCity) || currentPrice < cheapestPrice.get(neighborCity) || (currentPrice == cheapestPrice.get(neighborCity) && currentDuration < shortestDuration.get(neighborCity))) {
                        cheapestPrice.put(neighborCity, currentPrice);
                        shortestDuration.put(neighborCity, currentDuration);
                        cheapestFlights.put(neighborCity, currentCity);
                        citiesToVisit.offer(neighborCity);
                    }
                }
            }
        }

        // Build the cheapest route from the start to the end city
        List<Flight> cheapestRoute = new ArrayList<>();
        String currentCity = destinationCity;
        while (cheapestFlights.containsKey(currentCity)) {
            String previous = cheapestFlights.get(currentCity);
            for (Flight flight : flights) {
                if (flight.getOriginCity().equals(previous) && flight.getDestinationCity().equals(currentCity)) {
                    cheapestRoute.add(0, flight);
                    break;
                }
            }
            currentCity = previous;
        }

        return cheapestRoute;
    }
}
