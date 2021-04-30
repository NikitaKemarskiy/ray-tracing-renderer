package org.nikita.calculation;

import org.nikita.geometry.Ray;
import org.nikita.geometry.Triangle;
import org.nikita.geometry.Vector;
import org.nikita.renderer.TriangleIntersection;

public class MollerTrumboreRayTriangleIntersectionSolver implements RayTriangleIntersectionSolver {

    private static final double EPSILON = 1e-9;

    @Override
    public TriangleIntersection getRayTriangleIntersection(Ray ray, Triangle triangle) {
        Vector origin = ray.getOrigin();
        Vector direction = ray.getDirection();

        Vector vertex0 = triangle.getVertices().get(0);
        Vector vertex1 = triangle.getVertices().get(1);
        Vector vertex2 = triangle.getVertices().get(2);

        Vector edge1 = vertex1.subtract(vertex0);
        Vector edge2 = vertex2.subtract(vertex0);

        Vector h = direction.crossProduct(edge2);
        double a = edge1.dotProduct(h);

        if (a > -EPSILON && a < EPSILON) {
            return null;
        }

        double f = 1 / a;
        Vector s = origin.subtract(vertex0);
        double u = f * s.dotProduct(h);

        if (u < 0 || u > 1) {
            return null;
        }

        Vector q = s.crossProduct(edge1);
        double v = f * direction.dotProduct(q);

        if (v < 0 || u + v > 1) {
            return null;
        }

        double distance = f * edge2.dotProduct(q);

        if (distance <= EPSILON) {
            return null;
        }

        Vector point = direction.multiply(distance).add(origin);

        return new TriangleIntersection(triangle, point, distance);
    }
}
