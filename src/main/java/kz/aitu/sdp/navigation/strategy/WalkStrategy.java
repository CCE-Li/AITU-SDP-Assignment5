package kz.aitu.sdp.navigation.strategy;

import java.util.List;

import kz.aitu.sdp.navigation.domain.Route;
import kz.aitu.sdp.navigation.domain.Trip;

/**
 * Plans the trip on foot. Walking owns exactly one algorithm: pavement paths are
 * about 40&nbsp;% longer than the straight line (crossings, footbridges, no motorway
 * shortcuts), the pace is a brisk city walk, and the trip is free and emission-free.
 */
public final class WalkStrategy implements RouteStrategy {

    /** Pavement detours compared to the straight line: crossings, footbridges, stairs. */
    private static final double PAVEMENT_DETOUR = 1.4;
    private static final double WALKING_SPEED_KMH = 4.8;

    @Override
    public Route plan(Trip trip) {
        double km = trip.straightLineKm() * PAVEMENT_DETOUR;
        int minutes = RouteMath.minutesFor(km, WALKING_SPEED_KMH);

        List<String> steps = List.of(
                "Leave " + trip.from().name() + " on foot",
                "Follow the pavement for " + RouteMath.km(km) + " km (crossings and footbridges included)",
                "Arrive at " + trip.to().name());

        return new Route("Walking", km, minutes, 0, 0, steps);
    }
}
