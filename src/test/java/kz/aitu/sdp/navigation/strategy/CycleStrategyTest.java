package kz.aitu.sdp.navigation.strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import kz.aitu.sdp.navigation.domain.Location;
import kz.aitu.sdp.navigation.domain.Route;
import kz.aitu.sdp.navigation.domain.Trip;

/**
 * The cycling algorithm on the same 5 km straight line, proving that two strategies
 * answer the same trip with genuinely different numbers.
 */
class CycleStrategyTest {

    private static final double DELTA = 0.01;

    private static final Trip FIVE_KM_TRIP = new Trip(
            new Location("Home", 0, 0), new Location("Park", 3, 4));

    @Test
    void appliesTheBikeLaneDetourAndCommutingPace() {
        Route route = new CycleStrategy().plan(FIVE_KM_TRIP);

        assertEquals("Cycling", route.mode());
        assertEquals(6.25, route.distanceKm(), DELTA);       // 5 km x 1.25
        assertEquals(25, route.minutes());                   // 6.25 / 15 km/h = 25 min
        assertEquals(0.0, route.costKzt(), DELTA);
        assertEquals(0, route.co2Grams());
    }
}
