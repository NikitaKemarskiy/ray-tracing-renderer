package org.nikita.calculation;

import org.nikita.geometry.Triangle;
import org.nikita.geometry.Vector;

public interface RayTriangleIntersectionSolver {
    boolean intersects(Vector ray, Triangle triangle);
}
