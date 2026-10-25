package kz.aitu.sdp.navigation.strategy;

import java.util.List;

import kz.aitu.sdp.navigation.domain.Route;
import kz.aitu.sdp.navigation.domain.Trip;

/**
 * Plans the trip by bus and metro. Public transport owns one algorithm: the route
 * runs via stops and a transfer hub, door-to-door speed includes waiting and
 * stopping, the fare is charged per boarding, and a full bus emits little CO<sub>2</sub>
 * per passenger.
 *
 * <p>This class is also the demonstration of extensibility: it was added to the
 * project <em>after</em> the navigator, the interface and the first three strategies
 * were finished and tested - without changing a single existing class. The pattern
 * was open for extension and closed for modification.</p>
 */
public final class PublicTransportStrategy implements RouteStrategy {

    /** Stops and the transfer hub lengthen the path compared to the straight line. */
    private static final double STOP_DETOUR = 1.3;
    private static final double DOOR_TO_DOOR_KMH = 18.0;     // ride plus waits and stops

    private static final double FARE_KZT = 210.0;
    private static final double KM_PER_BOARDING = 7.0;       // roughly one transfer per 7 km
    private static final int CO2_GRAMS_PER_KM = 40;          // per passenger

    @Override
    public Route plan(Trip trip) {
        double km = trip.straightLineKm() * STOP_DETOUR;
        int minutes = RouteMath.minutesFor(km, DOOR_TO_DOOR_KMH);
        int boardings = Math.max(1, (int) Math.ceil(km / KM_PER_BOARDING));
        double cost = boardings * FARE_KZT;
        int co2 = (int) Math.round(km * CO2_GRAMS_PER_KM);

        List<String> steps = List.of(
                "Walk to the nearest stop from " + trip.from().name(),
                "Ride " + RouteMath.km(km) + " km with " + transfers(boardings),
                "Walk the last metres to " + trip.to().name());

        return new Route("Public transport", km, minutes, cost, co2, steps);
    }

    private static String transfers(int boardings) {
        int transfers = boardings - 1;
        return transfers == 0 ? "no transfer" : transfers + (transfers == 1 ? " transfer" : " transfers");
    }
}
