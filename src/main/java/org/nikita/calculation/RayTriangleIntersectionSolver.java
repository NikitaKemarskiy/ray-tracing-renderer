package org.nikita.calculation;

import org.nikita.geometry.Ray;
import org.nikita.geometry.Triangle;

public interface RayTriangleIntersectionSolver {
    double getRayTriangleIntersectionDistance(Ray ray, Triangle triangle);
}
