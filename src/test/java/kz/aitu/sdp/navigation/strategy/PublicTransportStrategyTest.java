package kz.aitu.sdp.navigation.strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import kz.aitu.sdp.navigation.domain.Location;
import kz.aitu.sdp.navigation.domain.Route;
import kz.aitu.sdp.navigation.domain.Trip;

/**
 * The strategy that was added last, without touching any existing class. Its tests
 * run against the same known inputs as the other three - the interface contract
 * made the new class testable on day one.
 */
class PublicTransportStrategyTest {

    private static final double DELTA = 0.01;

    private static final Trip FIVE_KM_TRIP = new Trip(
            new Location("Home", 0, 0), new Location("Park", 3, 4));

    private static final Trip TWENTY_KM_TRIP = new Trip(
            new Location("Home", 0, 0), new Location("Airport", 0, 20));

    @Test
    void chargesOneFareForAShortRide() {
        Route route = new PublicTransportStrategy().plan(FIVE_KM_TRIP);

        assertEquals("Public transport", route.mode());
        assertEquals(6.5, route.distanceKm(), DELTA);        // 5 km x 1.3
        assertEquals(22, route.minutes());                   // 6.5 / 18 km/h = 21.7 min
        assertEquals(210.0, route.costKzt(), DELTA);         // one boarding, one fare
        assertEquals(260, route.co2Grams());                 // 6.5 x 40
    }

    @Test
    void addsTransfersOnALongRide() {
        Route route = new PublicTransportStrategy().plan(TWENTY_KM_TRIP);

        assertEquals(26.0, route.distanceKm(), DELTA);       // 20 km x 1.3
        assertEquals(840.0, route.costKzt(), DELTA);         // ceil(26/7) = 4 boardings
        assertTrue(route.steps().get(1).contains("3 transfers"));
    }
}
