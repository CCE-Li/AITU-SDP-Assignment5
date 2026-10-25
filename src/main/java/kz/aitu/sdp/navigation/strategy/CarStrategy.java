package kz.aitu.sdp.navigation.strategy;

import java.util.List;

import kz.aitu.sdp.navigation.domain.Route;
import kz.aitu.sdp.navigation.domain.Trip;

/**
 * Plans the trip by car. Driving owns one algorithm: cars must respect one-way
 * streets and the ring road (the longest detour of the family), they move at city
 * pace door to door, they burn fuel and pay for parking - and they emit the most
 * CO<sub>2</sub> per kilometre of the family.
 */
public final class CarStrategy implements RouteStrategy {

    /** One-way streets and ring-road detours compared to the straight line. */
    private static final double STREET_DETOUR = 1.35;
    private static final double CITY_PACE_KMH = 30.0;

    private static final double FUEL_KZT_PER_KM = 18.0;      // 7.3 l/100 km at 245 KZT/l
    private static final double PARKING_KZT = 500.0;         // flat fee at the destination
    private static final int CO2_GRAMS_PER_KM = 150;

    @Override
    public Route plan(Trip trip) {
        double km = trip.straightLineKm() * STREET_DETOUR;
        int minutes = RouteMath.minutesFor(km, CITY_PACE_KMH);
        double cost = km * FUEL_KZT_PER_KM + PARKING_KZT;
        int co2 = (int) Math.round(km * CO2_GRAMS_PER_KM);

        List<String> steps = List.of(
                "Start the car at " + trip.from().name(),
                "Drive " + RouteMath.km(km) + " km through the city streets",
                "Park near " + trip.to().name() + " (parking fee included in the cost)");

        return new Route("Driving", km, minutes, cost, co2, steps);
    }
}
