package org.nikita.calculation;

import org.nikita.geometry.Triangle;
import org.nikita.geometry.Vector;

import java.util.Iterator;
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

        double triangleArea = triangle.getArea();

        Vector[] vertices = {
            triangle.getVertices().get(0),
            triangle.getVertices().get(1),
            triangle.getVertices().get(2)
        };

        Triangle[] innerTriangles = {
            new Triangle().addVertex(vertices[1]).addVertex(vertices[2]).addVertex(point),
            new Triangle().addVertex(vertices[0]).addVertex(vertices[2]).addVertex(point),
            new Triangle().addVertex(vertices[0]).addVertex(vertices[1]).addVertex(point)
        };

        for (int i = 0; i < vertices.length; i++) {
            Vector vertexNormal = verticesNormals.get(vertices[i]);
            trianglePointNormal = trianglePointNormal.add(
                vertexNormal.multiply(innerTriangles[i].getArea() / triangleArea)
            );
        }

        return trianglePointNormal;
    }
}
