package org.nikita.renderer;

import org.nikita.geometry.Axis;
import org.nikita.geometry.Vector;

import java.math.BigDecimal;

public class Screen {
    private final static double WIDTH = 1;
    private final static double HEIGHT = 1;

    private Vector center;
    private Axis widthAxis;
    private Axis heightAxis;
    private Axis normalAxis;
    private int pixelWidth;
    private int pixelHeight;

    public Screen(Vector center, Axis widthAxis, Axis heightAxis, Axis normalAxis, int pixelWidth, int pixelHeight) {
        this.center = center;
        this.widthAxis = widthAxis;
        this.heightAxis = heightAxis;
        this.normalAxis = normalAxis;
        this.pixelWidth = pixelWidth;
        this.pixelHeight = pixelHeight;
    }

    public Vector getCenter() {
        return center;
    }

    public double getMin(Axis axis) {
        if (axis == normalAxis) {
            return center.getCoordinateValue(axis);
        } else if (axis == widthAxis) {
            return center.getCoordinateValue(axis) - WIDTH / 2;
        } else {
            return center.getCoordinateValue(axis) - HEIGHT / 2;
        }
    }

    public double getMax(Axis axis) {
        if (axis == normalAxis) {
            return center.getCoordinateValue(axis);
        } else if (axis == widthAxis) {
            return center.getCoordinateValue(axis) + WIDTH / 2;
        } else {
            return center.getCoordinateValue(axis) + HEIGHT / 2;
        }
    }

    public double getWidthStep() {
        return WIDTH / pixelWidth;
    }

    public double getHeightStep() {
        return HEIGHT / pixelHeight;
    }

    public int getPixelWidth() {
        return pixelWidth;
    }

    public int getPixelHeight() {
        return pixelHeight;
    }

    public Axis getNormalAxis() {
        return normalAxis;
    }

    public Axis getWidthAxis() {
        return widthAxis;
    }

    public Axis getHeightAxis() {
        return heightAxis;
    }
}
