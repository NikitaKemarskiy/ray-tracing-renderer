package org.nikita.geometry;

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
        return switch (axis) {
            case X -> getX();
            case Y -> getY();
            case Z -> getZ();
        };
    }

    public Vector crossProduct(Vector vector) {
        return new Vector(
            this.y * vector.z - this.z * vector.y,
            this.z * vector.x - this.x * vector.z,
            this.x * vector.y - this.y * vector.x
        );
    }

    public double dotProduct(Vector vector) {
        return this.x * vector.x + this.y * vector.y + this.z * vector.z;
    }

    public Vector subtract(Vector vector) {
        return new Vector(
            this.x - vector.x,
            this.y - vector.y,
            this.z - vector.z
        );
    }

    @Override
    public String toString() {
        return String.format("(%.2f, %.2f, %.2f)", x, y, z);
    }
}
