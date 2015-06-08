package com.groep5.shakeyouup;

public class GPSController {
    public final static int R = 6371000; //Earth's radius in meters
	public static double getDistance(double x1, double x2, double y1, double y2) // x = latitude, y = longitude
    {
        double phi1 = Math.toRadians(x1);
        double phi2 = Math.toRadians(x2);
        double deltaPhi = Math.toRadians(x2 - x1);
        double deltaLambda = Math.toRadians(y2 - y1);

        double a = Math.sin(deltaPhi / 2) * Math.sin(deltaPhi / 2) +
                    Math.cos(phi1) * Math.cos(phi2) *
                    Math.sin(deltaLambda / 2) * Math.sin(deltaLambda / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distance = R * c;

        return distance;
    }
}
