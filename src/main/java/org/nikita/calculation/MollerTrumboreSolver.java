package org.nikita.calculation;

import org.nikita.geometry.Triangle;
import org.nikita.geometry.Vector;

import java.util.Iterator;

public class MollerTrumboreSolver implements RayTriangleIntersectionSolver {
    private static final double EPSILON = 1e-7;

    @Override
    public boolean intersects(Vector ray, Triangle triangle) {
        Iterator<Vector> verticesIterator = triangle.getVertices().iterator();

        Vector vector0 = verticesIterator.next();
        Vector vector1 = verticesIterator.next();
        Vector vector2 = verticesIterator.next();

        Vector edge1 = vector1.subtract(vector0);
        Vector edge2 = vector2.subtract(vector0);

        Vector h = ray.crossProduct(edge2);
        double a = edge1.dotProduct(h);

        if (a > -EPSILON && a < EPSILON) {
            return false;
        }

        double f = 1 / a;
        Vector s = ray.subtract(vector0);
        double u = f * s.dotProduct(h);

        if (u < 0 || u > 1) {
            return false;
        }

        Vector q = s.crossProduct(edge1);
        double v = f * ray.dotProduct(q);
        if (v < 0 || u + v > 1) {
            return false;
        }

        var t = f * edge2.dotProduct(q);

        return t > EPSILON;
    }
}
