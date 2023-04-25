package com.edreamsodigeo.travelling.salesman.service;


import com.edreamsodigeo.travellingsalesman.model.Flight;
import com.edreamsodigeo.travellingsalesman.store.FlightStore;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;


/**
 * Run tests on different data sets
 */
@RunWith(Parameterized.class)
public class RouteCalculatorTest {
    private static final FlightStore flightStore = mock(FlightStore.class);

    private RouteCalculator routeCalculator;

    @Parameterized.Parameter
    public Class routeCalculatorClass;

    @Parameterized.Parameters(name = "{index}: Impl Class: {0}")
    public static Collection classes(){
        List<Object[]> implementations = new ArrayList<>();
        implementations.add(new Object[]{RouteCalculatorImpl.class});
        implementations.add(new Object[]{RouteCalculatorOptimized.class});

        return implementations;
    }

    @Before
    public void setUp() throws Exception {
        routeCalculator = (RouteCalculator) routeCalculatorClass.getConstructor(FlightStore.class).newInstance(flightStore);
    }

    /**
     * Input: BCN-NYC
     * <p>
     * Storage:
     * BCN-NYC, 550€, 2022-01-13 12:00, 2022-01-13 15:00
     * BCN-ORY, 100€, 2022-01-13 12:00, 2022-01-13 14:00
     * ORY-NYC, 500€, 2022-01-13 15:00, 2022-01-13 18:00
     * <p>
     * Output: BCN-NYC
     */
    @Test
    public void testExampleDataSet1() {
        when(flightStore.getFlights()).thenReturn(
                List.of(
                        new Flight("BNC", "NYC", 550, 3),
                        new Flight("BNC", "ORY", 100, 2),
                        new Flight("ORY", "NYC", 500, 3)
                )
        );

        String originCity = "BNC";
        String destinationCity = "NYC";

        List<Flight> route = routeCalculator.calculate(originCity, destinationCity);

        assertEquals(route.size(), 1);
        assertEquals(route.get(0).getOriginCity(), originCity);
        assertEquals(route.get(0).getDestinationCity(), destinationCity);
    }

    /**
     * Input: BCN-NYC
     * <p>
     * Storage:
     * BCN-LON, 100€, 2022-01-13 12:00, 2022-01-13 15:00
     * BCN-ORY, 100€, 2022-01-13 12:00, 2022-01-13 14:00
     * LON-NYC, 500€, 2022-01-13 16:00, 2022-01-13 19:00
     * ORY-NYC, 500€, 2022-01-13 15:00, 2022-01-13 18:00
     * <p>
     * Output: BCN-ORY, ORY-NYC
     */
    @Test
    public void testExampleDataSet2() {

        when(flightStore.getFlights()).thenReturn(
                List.of(
                        new Flight("BNC", "LON", 100, 3),
                        new Flight("BNC", "ORY", 100, 2),
                        new Flight("LON", "NYC", 500, 3),
                        new Flight("ORY", "NYC", 500, 3)
                )
        );

        String originCity = "BNC";
        String destinationCity = "NYC";
        String intermediateCity = "ORY";

        List<Flight> route = routeCalculator.calculate(originCity, destinationCity);

        assertEquals(route.size(), 2);
        assertEquals(route.get(0).getOriginCity(), originCity);
        assertEquals(route.get(0).getDestinationCity(), intermediateCity);
        assertEquals(route.get(1).getOriginCity(), intermediateCity);
        assertEquals(route.get(1).getDestinationCity(), destinationCity);
    }


    /**
     * Input: BCN-HND
     * <p>
     * Storage:
     * <p>
     * BCN-MAD, 10€, 2022-01-13 12:00, 2022-01-13 14:00
     * BCN-PAR, 50€, 2022-01-13 12:00, 2022-01-13 14:00
     * PAR-LIS, 10€, 2022-01-13 15:00, 2022-01-13 17:00
     * PAR-MNL, 50€, 2022-01-13 15:00, 2022-01-13 17:00
     * BCN-SVQ, 100€, 2022-01-13 12:00, 2022-01-13 14:00
     * MNL-HND, 500€, 2022-01-13 18:00, 2022-01-14 04:00
     * MAD-LON, 10€, 2022-01-13 15:00, 2022-01-13 17:00
     * WAW-NYC, 500€, 2022-01-13 21:00, 2022-01-14 02:00
     * SVQ-WAW, 500€, 2022-01-13 15:00, 2022-01-13 20:00
     * NYC-HND, 500€, 2022-01-14 03:00, 2022-01-14 08:00
     * <p>
     * Output: BCN-PAR, PAR-MNL, MNL-HND
     */
    @Test
    public void testExampleDataSet3() {

        when(flightStore.getFlights()).thenReturn(
                List.of(
                        new Flight("BNC", "MAD", 10, 2),
                        new Flight("BNC", "PAR", 50, 2),
                        new Flight("PAR", "LIS", 10, 2),
                        new Flight("PAR", "MNL", 50, 2),
                        new Flight("BNC", "SVQ", 100, 2),
                        new Flight("MNL", "HND", 500, 10),
                        new Flight("MAD", "LON", 10, 2),
                        new Flight("WAW", "NYC", 500, 5),
                        new Flight("SVQ", "WAW", 500, 5),
                        new Flight("NYC", "HND", 500, 5)
                )
        );

        String originCity = "BNC";
        String destinationCity = "HND";

        String intermediateCity1 = "PAR";
        String intermediateCity2 = "MNL";

        List<Flight> route = routeCalculator.calculate(originCity, destinationCity);

        assertEquals(route.size(), 3);
        assertEquals(route.get(0).getOriginCity(), originCity);
        assertEquals(route.get(0).getDestinationCity(), intermediateCity1);
        assertEquals(route.get(1).getOriginCity(), intermediateCity1);
        assertEquals(route.get(1).getDestinationCity(), intermediateCity2);
        assertEquals(route.get(2).getOriginCity(), intermediateCity2);
        assertEquals(route.get(2).getDestinationCity(), destinationCity);
    }
}
