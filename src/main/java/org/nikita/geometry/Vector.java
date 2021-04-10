package org.nikita.geometry;

import java.util.stream.Collectors;

public class Vector {
    private double x;
    private double y;
    private double z;

    public static final Vector ZERO = new Vector(0, 0, 0);

    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double getCoordinateValue(Axis axis) {
        switch (axis) {
            case X:
                return getX();
            case Y:
                return getY();
            case Z:
                return getZ();
            default:
                return 0;
        }
    }

    @Override
    public String toString() {
        return String.format("(%.2f, %.2f, %.2f)", x, y, z);
    }
}
