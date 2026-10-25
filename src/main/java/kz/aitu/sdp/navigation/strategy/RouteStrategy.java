package kz.aitu.sdp.navigation.strategy;

import kz.aitu.sdp.navigation.domain.Route;
import kz.aitu.sdp.navigation.domain.Trip;

/**
 * One algorithm for planning a route. Every concrete strategy answers the same
 * question - "how do I get from here to there?" - with its own rules about paths,
 * speeds, fares and emissions. The interface is the whole contract: one method,
 * no strategy knows that the others exist.
 */
public interface RouteStrategy {

    /** Plans the given trip and returns the route this strategy produces for it. */
    Route plan(Trip trip);
}
