package org.nikita.calculation;

import org.nikita.geometry.Triangle;
import org.nikita.geometry.Vector;

public class NormalTriangleColorIntensitySolver implements TriangleColorIntensitySolver {
    private double ambientLightIntensity;
    private Vector lightSourcePosition;

    public NormalTriangleColorIntensitySolver(double ambientLightIntensity, Vector lightSourcePosition) {
        this.ambientLightIntensity = ambientLightIntensity;
        this.lightSourcePosition = lightSourcePosition;
    }

    @Override
    public double getTrianglePointColorIntensity(Triangle triangle, Vector point) {
        Vector lightSourceDirection = lightSourcePosition.subtract(point);
        Vector triangleNormal = triangle.getNormal();

        double lightSourceDirectionLength = lightSourceDirection.length();
        double triangleNormalLength = triangleNormal.length();

        double cos = lightSourceDirection.multiply(triangleNormal) / (lightSourceDirectionLength * triangleNormalLength);

        return Math.max(ambientLightIntensity, cos);
    }
}
