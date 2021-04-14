package org.nikita.calculation;

import org.nikita.geometry.Ray;
import org.nikita.geometry.Triangle;
import org.nikita.renderer.TriangleIntersection;

public interface RayTriangleIntersectionSolver {

    TriangleIntersection getRayTriangleIntersection(Ray ray, Triangle triangle);
}
