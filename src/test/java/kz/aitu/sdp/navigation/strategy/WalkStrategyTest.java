package kz.aitu.sdp.navigation.strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import kz.aitu.sdp.navigation.domain.Location;
import kz.aitu.sdp.navigation.domain.Route;
import kz.aitu.sdp.navigation.domain.Trip;

/**
 * The walking algorithm on a trip whose straight line is exactly 5 km, so every
 * number below is the strategy's own rule applied to a known input.
 */
class WalkStrategyTest {

    private static final double DELTA = 0.01;

    /** (0,0) to (3,4): a 3-4-5 triangle, straight line exactly 5 km. */
    private static final Trip FIVE_KM_TRIP = new Trip(
            new Location("Home", 0, 0), new Location("Park", 3, 4));

    @Test
    void appliesThePavementDetourAndWalkingPace() {
        Route route = new WalkStrategy().plan(FIVE_KM_TRIP);

        assertEquals("Walking", route.mode());
        assertEquals(7.0, route.distanceKm(), DELTA);        // 5 km x 1.4
        assertEquals(88, route.minutes());                   // 7.0 / 4.8 km/h = 87.5 min
        assertEquals(0.0, route.costKzt(), DELTA);
        assertEquals(0, route.co2Grams());
    }

    @Test
    void tellsTheTravellerWhereToStartAndFinish() {
        Route route = new WalkStrategy().plan(FIVE_KM_TRIP);

        assertTrue(route.steps().get(0).contains("Home"));
        assertTrue(route.steps().get(route.steps().size() - 1).contains("Park"));
    }
}
