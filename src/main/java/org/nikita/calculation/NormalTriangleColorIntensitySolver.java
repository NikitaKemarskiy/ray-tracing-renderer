package org.nikita.calculation;

import org.nikita.geometry.Triangle;
import org.nikita.geometry.Vector;

import java.util.Map;

public class NormalTriangleColorIntensitySolver implements TriangleColorIntensitySolver {
    private double ambientLightIntensity;
    private Vector lightSourcePosition;
    private Map<Vector, Vector> verticesNormals;

    public NormalTriangleColorIntensitySolver(
        double ambientLightIntensity,
        Vector lightSourcePosition,
        Map<Vector, Vector> verticesNormals
    ) {
        this.ambientLightIntensity = ambientLightIntensity;
        this.lightSourcePosition = lightSourcePosition;
        this.verticesNormals = verticesNormals;
    }

    @Override
    public double getTrianglePointColorIntensity(Triangle triangle, Vector point) {
        Vector lightSourceDirection = lightSourcePosition.subtract(point);
        Vector triangleNormal = getTrianglePointNormal(triangle, point);

        double lightSourceDirectionLength = lightSourceDirection.length();
        double triangleNormalLength = triangleNormal.length();

        double cos = lightSourceDirection.multiply(triangleNormal) / (lightSourceDirectionLength * triangleNormalLength);

        return Math.max(ambientLightIntensity, cos);
    }

    public Vector getTrianglePointNormal(Triangle triangle, Vector point) {
        Vector trianglePointNormal = new Vector(0, 0, 0);

        double totalDistanceReverse = triangle.getVertices()
            .stream()
            .map(vertex -> 1 / vertex.getDistance(point))
            .reduce(0.0, Double::sum);

        double totalDistance = triangle.getVertices()
            .stream()
            .map(vertex -> vertex.getDistance(point))
            .reduce(0.0, Double::sum);

        for (Vector vertex : triangle.getVertices()) {
            Vector vertexNormal = verticesNormals.get(vertex);
            double vertexToPointDistanceReverse = 1 / vertex.getDistance(point);

            trianglePointNormal = trianglePointNormal.add(
                vertexNormal.multiply(vertexToPointDistanceReverse / totalDistanceReverse)
            );
        }

        return trianglePointNormal;
    }
}
