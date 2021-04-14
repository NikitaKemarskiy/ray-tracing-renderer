package org.nikita.calculation;

import org.nikita.geometry.Triangle;
import org.nikita.geometry.Vector;

public interface RayTriangleIntersectionSolver {
    double intersects(Vector from, Vector ray, Triangle triangle);
}
