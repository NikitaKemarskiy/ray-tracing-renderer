package org.nikita.calculation;

import org.nikita.geometry.Ray;
import org.nikita.geometry.Triangle;

public interface RayTriangleIntersectionSolver {
    double intersects(Ray ray, Triangle triangle);
}
