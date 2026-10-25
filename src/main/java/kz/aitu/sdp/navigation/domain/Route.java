package kz.aitu.sdp.navigation.domain;

import java.util.List;

/**
 * The answer a strategy gives to a trip: which travel mode it used, how long the
 * path is, how much time, money and CO<sub>2</sub> it costs, and the human-readable
 * steps of the route. Immutable - a planned route never changes after it is returned.
 */
public record Route(String mode, double distanceKm, int minutes, double costKzt,
                    int co2Grams, List<String> steps) {

    public Route {
        if (distanceKm < 0 || minutes < 0 || costKzt < 0 || co2Grams < 0) {
            throw new IllegalArgumentException("A route cannot have negative numbers");
        }
        steps = List.copyOf(steps);
    }
}
