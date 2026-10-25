package kz.aitu.sdp.navigation;

import kz.aitu.sdp.navigation.domain.Location;

/**
 * The places of the demo, defined in one place so that the coordinates used by the
 * sample data and by the client can never drift apart. Coordinates are kilometres
 * on a simplified Astana city grid.
 */
public final class CityMap {

    public static final Location AITU = new Location("Astana IT University", 0.0, 0.0);
    public static final Location BAITEREK = new Location("Baiterek Tower", 2.8, 1.9);
    public static final Location KHAN_SHATYR = new Location("Khan Shatyr", 4.2, 3.4);
    public static final Location EXPO_PAVILION = new Location("Expo Pavilion", 1.5, 4.6);
    public static final Location RAILWAY_STATION = new Location("Railway Station", 5.1, 1.2);
    public static final Location AIRPORT = new Location("Nursultan Nazarbayev Airport", 9.6, 7.2);

    private CityMap() {
    }
}
