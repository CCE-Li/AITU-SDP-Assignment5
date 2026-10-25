package kz.aitu.sdp.navigation.domain;

import java.util.Objects;

/**
 * Where the traveller starts and where they want to be. A trip is the same question
 * asked of every strategy: "how do I get from here to there?"
 */
public record Trip(Location from, Location to) {

    public Trip {
        Objects.requireNonNull(from, "A trip needs a starting point");
        Objects.requireNonNull(to, "A trip needs a destination");
    }

    /** Straight-line distance of the trip, in kilometres. */
    public double straightLineKm() {
        return from.distanceKm(to);
    }
}
