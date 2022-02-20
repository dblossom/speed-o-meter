package com.danielblossom.speed_o_meter;

import android.location.Location;

public class MyLocation extends Location {

    public MyLocation(Location location) {
        super(location);
    }

    @Override
    public float distanceTo(Location dest) {
        // meters to feet
        return super.distanceTo(dest) * 3.28083989501312f;
    }

    @Override
    public double getAltitude() {
        // meters to feet
        return super.getAltitude() * 3.28083989501312d;
    }

    @Override
    public float getSpeed() {
        // getSpeed() * 3.6f for meters/second
        // m/s to MPH
        return super.getSpeed() * 2.23693629f;
    }

    @Override
    public float getAccuracy() {
        // meters to feet
        return super.getAccuracy() * 3.28083989501312f;
    }
}
