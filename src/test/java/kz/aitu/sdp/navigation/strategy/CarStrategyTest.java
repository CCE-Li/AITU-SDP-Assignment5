package kz.aitu.sdp.navigation.strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import kz.aitu.sdp.navigation.domain.Location;
import kz.aitu.sdp.navigation.domain.Route;
import kz.aitu.sdp.navigation.domain.Trip;

/**
 * The driving algorithm: the longest detour of the family, fuel plus parking, and
 * by far the most CO2.
 */
class CarStrategyTest {

    private static final double DELTA = 0.01;

    private static final Trip FIVE_KM_TRIP = new Trip(
            new Location("Home", 0, 0), new Location("Park", 3, 4));

    @Test
    void appliesTheStreetDetourAndCityPace() {
        Route route = new CarStrategy().plan(FIVE_KM_TRIP);

        assertEquals("Driving", route.mode());
        assertEquals(6.75, route.distanceKm(), DELTA);       // 5 km x 1.35
        assertEquals(14, route.minutes());                   // 6.75 / 30 km/h = 13.5 min
        assertEquals(621.5, route.costKzt(), DELTA);         // 6.75 x 18 + 500
        assertEquals(1013, route.co2Grams());                // 6.75 x 150 = 1012.5
    }

    @Test
    void mentionsTheParkingInTheSteps() {
        Route route = new CarStrategy().plan(FIVE_KM_TRIP);

        assertTrue(route.steps().get(route.steps().size() - 1).contains("Park"));
        assertTrue(route.steps().stream().anyMatch(step -> step.contains("parking")));
    }
}
