package org.nikita.calculation;

import org.nikita.geometry.Triangle;
import org.nikita.geometry.Vector;

import java.util.Iterator;

public class NormalTriangleColorIntensitySolver implements TriangleColorIntensitySolver {
    private double ambientLightIntensity;

    public NormalTriangleColorIntensitySolver(double ambientLightIntensity) {
        this.ambientLightIntensity = ambientLightIntensity;
    }

    @Override
    public double getTriangleColorIntensity(Triangle triangle, Vector lightSourcePosition) {
        Vector triangleCentroid = triangle.getCentroid();
        Vector lightSourceDirection = lightSourcePosition.subtract(triangleCentroid);

        Iterator<Vector> verticesIterator = triangle.getVertices().iterator();

        Vector vertex0 = verticesIterator.next();
        Vector vertex1 = verticesIterator.next();
        Vector vertex2 = verticesIterator.next();

        Vector edge1 = vertex1.subtract(vertex0);
        Vector edge2 = vertex2.subtract(vertex0);

        Vector triangleNormal = edge1.crossProduct(edge2);

        double lightSourceDirectionLength = lightSourceDirection.length();
        double triangleNormalLength = triangleNormal.length();
        double l = lightSourceDirection.subtract(triangleNormal).length();

        double cos = (
            lightSourceDirectionLength * lightSourceDirectionLength +
            triangleNormalLength * triangleNormalLength +
            l * l
        ) / (2 * lightSourceDirectionLength * triangleNormalLength);

        return Math.max(ambientLightIntensity, cos);
    }
}
