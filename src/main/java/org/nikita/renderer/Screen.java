package org.nikita.renderer;

import org.nikita.geometry.Axis;
import org.nikita.geometry.Vector;

public class Screen {
    private Vector center;
    /**
     * Parallel to means that screen is parallel to the
     * specified axis. For example, if parallelTo == Axis.X,
     * then width is considered as size by X axis
     */
    private Axis parallelTo;
    /**
     * Parallel to means that screen is perpendicular to the
     * specified axis. For example, if parallelTo == Axis.X and normalTo == Axis.Z,
     * then height is considered as size by Y axis
     */
    private Axis normalTo;
    private int width;
    private int height;

    public Screen(Vector center, Axis parallelTo, Axis normalTo, int width, int height) {
        this.center = center;
        this.parallelTo = parallelTo;
        this.normalTo = normalTo;
        this.width = width;
        this.height = height;
    }

    public Vector getCenter() {
        return center;
    }

    public void setCenter(Vector center) {
        this.center = center;
    }

    public double getMin(Axis axis) {
        return normalTo == axis
            ? center.getCoordinateValue(axis)
            : center.getCoordinateValue(axis) - (
                parallelTo == axis ? width : height
            );
    }

    public double getMax(Axis axis) {
        return normalTo == axis
            ? center.getCoordinateValue(axis)
            : center.getCoordinateValue(axis) + (
            parallelTo == axis ? width : height
        );
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
