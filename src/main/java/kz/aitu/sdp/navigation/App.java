package kz.aitu.sdp.navigation;

import java.util.List;

import kz.aitu.sdp.navigation.domain.Route;
import kz.aitu.sdp.navigation.domain.Trip;
import kz.aitu.sdp.navigation.strategy.CarStrategy;
import kz.aitu.sdp.navigation.strategy.CycleStrategy;
import kz.aitu.sdp.navigation.strategy.PublicTransportStrategy;
import kz.aitu.sdp.navigation.strategy.RouteStrategy;
import kz.aitu.sdp.navigation.strategy.WalkStrategy;

/**
 * The client. It owns the choice: which strategy the navigator starts with, when to
 * switch, and which strategies take part in a comparison. The navigator itself
 * never chooses - it only delegates to whatever strategy it was given.
 */
public final class App {

    /** The whole family, listed in the one place that configures strategies: the client. */
    private static final List<RouteStrategy> FAMILY = List.of(
            new WalkStrategy(),
            new CycleStrategy(),
            new CarStrategy(),
            new PublicTransportStrategy());

    public static void main(String[] args) {
        Trip airportTrip = new Trip(CityMap.AITU, CityMap.AIRPORT);

        demoDefaultStrategy(airportTrip);
        demoRuntimeSwitching(airportTrip);
        demoFamilyComparison(airportTrip);
        demoShortTrip();

        System.out.println("\nDemo finished.");
    }

    private static void demoDefaultStrategy(Trip trip) {
        title("1. Plan the airport trip with the default strategy (driving)");
        Navigator navigator = new Navigator(new CarStrategy());
        printRoute(navigator.planTrip(trip));
    }

    private static void demoRuntimeSwitching(Trip trip) {
        title("2. Same navigator, same call - the strategy is switched at runtime");

        Navigator navigator = new Navigator(FAMILY.get(0));
        for (RouteStrategy strategy : FAMILY) {
            navigator.useStrategy(strategy);
            line("after useStrategy(" + strategy.getClass().getSimpleName() + ")",
                    navigator.planTrip(trip).mode());
        }
    }

    private static void demoFamilyComparison(Trip trip) {
        title("3. Compare the whole family on the same trip");
        System.out.printf("  %-18s %10s %12s %14s %10s%n",
                "mode", "distance", "time", "cost", "CO2");
        for (Route route : planWithEveryStrategy(trip)) {
            System.out.printf("  %-18s %10s %12s %14s %10s%n",
                    route.mode(), kilometres(route.distanceKm()), duration(route.minutes()),
                    money(route.costKzt()), route.co2Grams() + " g");
        }
    }

    private static void demoShortTrip() {
        title("4. A short city trip - the client picks a different winner for a different question");
        Trip trip = new Trip(CityMap.AITU, CityMap.KHAN_SHATYR);
        for (Route route : planWithEveryStrategy(trip)) {
            System.out.printf("  %-18s %10s %12s %14s %10s%n",
                    route.mode(), kilometres(route.distanceKm()), duration(route.minutes()),
                    money(route.costKzt()), route.co2Grams() + " g");
        }
    }

    /** The table drives the whole family through one navigator, switching as it goes. */
    private static List<Route> planWithEveryStrategy(Trip trip) {
        Navigator navigator = new Navigator(FAMILY.get(0));
        return FAMILY.stream()
                .map(strategy -> {
                    navigator.useStrategy(strategy);
                    return navigator.planTrip(trip);
                })
                .toList();
    }

    private static void printRoute(Route route) {
        line("mode", route.mode());
        line("distance", kilometres(route.distanceKm()));
        line("time", duration(route.minutes()));
        line("cost", money(route.costKzt()));
        line("CO2", route.co2Grams() + " g");
        route.steps().forEach(step -> System.out.println("    - " + step));
    }

    private static void title(String text) {
        System.out.println("\n=== " + text + " ===");
    }

    private static void line(String label, Object value) {
        System.out.printf("  %-42s %s%n", label, value);
    }

    private static String kilometres(double km) {
        return String.format("%.1f km", km);
    }

    private static String duration(int minutes) {
        return minutes < 60 ? minutes + " min"
                : String.format("%d h %02d min", minutes / 60, minutes % 60);
    }

    private static String money(double amount) {
        return String.format("%,.0f KZT", amount);
    }
}
