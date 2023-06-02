package com.bafia;

import java.util.Random;

public class Vector {
    static Random rand = new Random();
    double x;
    double y;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector(double maxRadius) {
        double r = rand.nextDouble(0*maxRadius, maxRadius);
        double ang = rand.nextDouble() * 2 * Math.PI;
        this.x = r*Math.cos(ang);
        this.y = r*Math.sin(ang);
    }

    public void add(Vector v) {
        x += v.x;
        y += v.y;
    }

    public double getDistance(Vector v) {
        return Math.sqrt((x-v.x)*(x-v.x)+(y-v.y)*(y-v.y));
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
