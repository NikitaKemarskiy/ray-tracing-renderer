package org.nikita.geometry;

import java.util.Objects;

public class Vector {
    private double x;
    private double y;
    private double z;

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

    public void setCoordinateValue(double value, Axis axis) {
        switch (axis) {
            case X -> setX(value);
            case Y -> setY(value);
            case Z -> setZ(value);
        };
    }

    public double length() {
        return Math.sqrt(x * x + y * y + z * z);
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

    public Vector add(Vector vector) {
        return new Vector(
            this.x + vector.x,
            this.y + vector.y,
            this.z + vector.z
        );
    }

    public Vector subtract(Vector vector) {
        return new Vector(
            this.x - vector.x,
            this.y - vector.y,
            this.z - vector.z
        );
    }

    public double multiply(Vector vector) {
        return x * vector.getX() + y * vector.getY() + z * vector.getZ();
    }

    public Vector multiply(double value) {
        return new Vector(
            this.x * value,
            this.y * value,
            this.z * value
        );
    }

    public Vector divide(Vector vector) {
        return new Vector(
            this.x / vector.x,
            this.y / vector.y,
            this.z / vector.z
        );
    }

    public Vector divide(double value) {
        return new Vector(
            this.x / value,
            this.y / value,
            this.z / value
        );
    }

    public double getDistance(Vector vector) {
        return Math.sqrt(Math.pow(x - vector.getX(), 2) + Math.pow(y - vector.getY(), 2) + Math.pow(z - vector.getZ(), 2));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector vector = (Vector) o;
        return Double.compare(vector.x, x) == 0 &&
                Double.compare(vector.y, y) == 0 &&
                Double.compare(vector.z, z) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public String toString() {
        return String.format("(%f, %f, %f)", x, y, z);
    }
}
