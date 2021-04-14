package org.nikita.structure;

import org.nikita.geometry.Ray;
import org.nikita.geometry.Triangle;

public interface TriangleTree {
    void addTriangle(Triangle triangle);
    double getTriangleIntersectionDistanceWithRay(Ray ray);
}
