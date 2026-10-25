package kz.aitu.sdp.navigation.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * A route is a value: it cannot be edited after it is planned, and it refuses to
 * carry impossible numbers.
 */
class RouteTest {

    @Test
    void keepsItsStepsImmutable() {
        List<String> steps = new ArrayList<>(List.of("Leave home", "Arrive"));
        Route route = new Route("Walking", 2.0, 25, 0, 0, steps);

        steps.set(0, "edited behind the route's back");

        assertEquals("Leave home", route.steps().get(0));
        assertThrows(UnsupportedOperationException.class, () -> route.steps().set(0, "edited"));
    }

    @Test
    void refusesNegativeNumbers() {
        assertThrows(IllegalArgumentException.class,
                () -> new Route("Driving", -1.0, 10, 0, 0, List.of()));
    }
}
