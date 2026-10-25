package kz.aitu.sdp.navigation.strategy;

import java.util.List;

import kz.aitu.sdp.navigation.domain.Route;
import kz.aitu.sdp.navigation.domain.Trip;

/**
 * Plans the trip by bicycle. Cycling owns one algorithm: bike lanes follow the
 * streets but skip the one-way detours, the speed is a relaxed commuting pace, and
 * riding your own bike costs nothing and emits nothing.
 */
public final class CycleStrategy implements RouteStrategy {

    /** Bike lanes cut the detour: streets, but no one-way loops and no ring road. */
    private static final double BIKE_LANE_DETOUR = 1.25;
    private static final double CYCLING_SPEED_KMH = 15.0;

    @Override
    public Route plan(Trip trip) {
        double km = trip.straightLineKm() * BIKE_LANE_DETOUR;
        int minutes = RouteMath.minutesFor(km, CYCLING_SPEED_KMH);

        List<String> steps = List.of(
                "Mount your bike at " + trip.from().name(),
                "Follow the bike lanes for " + RouteMath.km(km) + " km",
                "Leave the bike at a stand near " + trip.to().name());

        return new Route("Cycling", km, minutes, 0, 0, steps);
    }
}
