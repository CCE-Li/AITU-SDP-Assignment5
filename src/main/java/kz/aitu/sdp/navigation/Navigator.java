package kz.aitu.sdp.navigation;

import java.util.Objects;

import kz.aitu.sdp.navigation.domain.Route;
import kz.aitu.sdp.navigation.domain.Trip;
import kz.aitu.sdp.navigation.strategy.RouteStrategy;

/**
 * The Context of the Strategy pattern. A navigator plans trips, but it has no
 * route-planning algorithm of its own: it holds whatever {@link RouteStrategy} the
 * traveller chose and delegates every request to it. It never asks which strategy
 * it is holding - there is no {@code if} and no {@code switch} on a strategy type
 * here - and switching the strategy at runtime is one method call.
 */
public class Navigator {

    private RouteStrategy routeStrategy;

    public Navigator(RouteStrategy initialStrategy) {
        useStrategy(initialStrategy);
    }

    /** Replaces the current strategy; the next planned trip already uses the new one. */
    public void useStrategy(RouteStrategy strategy) {
        this.routeStrategy = Objects.requireNonNull(strategy, "A navigator needs a route strategy");
    }

    /** Delegates the whole planning decision to the current strategy. */
    public Route planTrip(Trip trip) {
        return routeStrategy.plan(trip);
    }
}
