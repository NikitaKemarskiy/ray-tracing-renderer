package org.nikita.calculation;

import org.nikita.geometry.Triangle;
import org.nikita.geometry.Vector;

import java.util.Iterator;

public class MollerTrumboreSolver implements RayTriangleIntersectionSolver {
    private static final double EPSILON = 1e-8;

    @Override
    public double intersects(Vector from, Vector ray, Triangle triangle) {
        Iterator<Vector> verticesIterator = triangle.getVertices().iterator();

        Vector vertex0 = verticesIterator.next();
        Vector vertex1 = verticesIterator.next();
        Vector vertex2 = verticesIterator.next();

        Vector edge1 = vertex1.subtract(vertex0);
        Vector edge2 = vertex2.subtract(vertex0);

        Vector h = ray.crossProduct(edge2);
        double a = edge1.dotProduct(h);

        if (a > -EPSILON && a < EPSILON) {
            return -1;
        }

        double f = 1 / a;
        Vector s = from.subtract(vertex0);
        double u = f * s.dotProduct(h);

        if (u < 0 || u > 1) {
            return -1;
        }

        Vector q = s.crossProduct(edge1);
        double v = f * ray.dotProduct(q);
        if (v < 0 || u + v > 1) {
            return -1;
        }

        var t = f * edge2.dotProduct(q);

        return t;
    }
}
