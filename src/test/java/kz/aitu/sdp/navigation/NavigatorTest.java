package kz.aitu.sdp.navigation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;

import kz.aitu.sdp.navigation.domain.Location;
import kz.aitu.sdp.navigation.domain.Route;
import kz.aitu.sdp.navigation.domain.Trip;
import kz.aitu.sdp.navigation.strategy.CarStrategy;
import kz.aitu.sdp.navigation.strategy.CycleStrategy;
import kz.aitu.sdp.navigation.strategy.RouteStrategy;
import kz.aitu.sdp.navigation.strategy.WalkStrategy;

/**
 * What the Context promises: it delegates to whatever strategy it holds, it switches
 * at runtime, and it knows nothing about any concrete strategy.
 */
class NavigatorTest {

    private static final Trip TRIP = new Trip(
            new Location("Home", 0, 0), new Location("Park", 3, 4));

    @Test
    void delegatesToTheStrategyItWasBuiltWith() {
        Navigator navigator = new Navigator(new WalkStrategy());

        Route planned = navigator.planTrip(TRIP);
        Route expected = new WalkStrategy().plan(TRIP);

        assertEquals(expected, planned);
    }

    @Test
    void switchesStrategyAtRuntime() {
        Navigator navigator = new Navigator(new CarStrategy());
        Route byCar = navigator.planTrip(TRIP);

        navigator.useStrategy(new CycleStrategy());
        Route byBike = navigator.planTrip(TRIP);

        assertEquals(new CarStrategy().plan(TRIP), byCar);
        assertEquals(new CycleStrategy().plan(TRIP), byBike);
    }

    @Test
    void refusesToWorkWithoutAStrategy() {
        Navigator navigator = new Navigator(new WalkStrategy());

        assertThrows(NullPointerException.class, () -> new Navigator(null));
        assertThrows(NullPointerException.class, () -> navigator.useStrategy(null));
    }

    /**
     * The pattern's key promise, checked mechanically: the Context source mentions
     * no concrete strategy and contains no branch on a strategy - if someone later
     * adds an "if the strategy is a CarStrategy" shortcut, this test fails.
     */
    @Test
    void knowsNothingAboutConcreteStrategies() throws IOException {
        Path source = Path.of("src", "main", "java", "kz", "aitu", "sdp", "navigation",
                "Navigator.java");
        assumeTrue(Files.exists(source), "source file not found - run with Maven from the project root");

        String codeOnly = Files.readString(source)
                .replaceAll("(?s)/\\*.*?\\*/", "")   // block comments, incl. the Javadoc
                .replaceAll("//[^\n]*", "");          // line comments

        List<String> forbidden = List.of(
                "WalkStrategy", "CycleStrategy", "CarStrategy", "PublicTransportStrategy",
                "instanceof", "switch", "if (", "if(");

        for (String token : forbidden) {
            assertTrue(!codeOnly.contains(token),
                    "Navigator must not contain '" + token + "' - the context is algorithm-agnostic");
        }
    }
}
