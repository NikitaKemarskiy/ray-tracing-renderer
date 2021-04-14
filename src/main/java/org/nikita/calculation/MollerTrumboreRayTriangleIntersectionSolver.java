package org.nikita.calculation;

import org.nikita.geometry.Ray;
import org.nikita.geometry.Triangle;
import org.nikita.geometry.Vector;
import org.nikita.renderer.TriangleIntersection;

import java.util.Iterator;

public class MollerTrumboreRayTriangleIntersectionSolver implements RayTriangleIntersectionSolver {

    private static final double EPSILON = 1e-8;

    @Override
    public TriangleIntersection getRayTriangleIntersection(Ray ray, Triangle triangle) {
        Vector origin = ray.getOrigin();
        Vector direction = ray.getDirection();

        Iterator<Vector> verticesIterator = triangle.getVertices().iterator();

        Vector vertex0 = verticesIterator.next();
        Vector vertex1 = verticesIterator.next();
        Vector vertex2 = verticesIterator.next();

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
