package kz.aitu.sdp.navigation.strategy;

/**
 * The two little calculations every strategy needs. They live here so that no
 * concrete strategy ever depends on another concrete strategy - each one uses this
 * package-private helper and nothing else.
 */
final class RouteMath {

    private RouteMath() {
    }

    /** Travel time for a distance at a given speed, rounded to whole minutes. */
    static int minutesFor(double km, double speedKmh) {
        return (int) Math.round(km / speedKmh * 60);
    }

    /** A distance for human-readable route steps, e.g. {@code "12.4"}. */
    static String km(double km) {
        return String.format("%.1f", km);
    }
}
