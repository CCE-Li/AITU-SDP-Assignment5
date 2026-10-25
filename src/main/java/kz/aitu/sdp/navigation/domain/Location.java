package kz.aitu.sdp.navigation.domain;

/**
 * A named place on the city map. Coordinates are kilometres on the demo city grid,
 * so distances can be computed locally without an external maps service.
 */
public record Location(String name, double x, double y) {

    public Location {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("A location needs a name");
        }
    }

    /** Straight-line distance between two places, in kilometres. */
    public double distanceKm(Location other) {
        double dx = x - other.x;
        double dy = y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
}
