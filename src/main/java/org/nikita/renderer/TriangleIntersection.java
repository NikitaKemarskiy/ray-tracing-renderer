package org.nikita.renderer;

import org.nikita.geometry.Triangle;
import org.nikita.geometry.Vector;

public class TriangleIntersection {

    private Triangle triangle;
    private Vector point;
    private double distance;

    public TriangleIntersection(Triangle triangle, Vector point, double distance) {
        this.triangle = triangle;
        this.point = point;
        this.distance = distance;
    }

    public Triangle getTriangle() {
        return triangle;
    }

    public Vector getPoint() {
        return point;
    }

    public double getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return "TriangleIntersection{" +
                "triangle=" + triangle +
                ", point=" + point +
                ", distance=" + distance +
                '}';
    }
}
