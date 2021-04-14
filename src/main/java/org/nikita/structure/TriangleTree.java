package org.nikita.structure;

import org.nikita.geometry.Triangle;
import org.nikita.geometry.Vector;

public interface TriangleTree {
    void addTriangle(Triangle triangle);
    double getTriangleIntersectionDistanceWithRay(Vector from, Vector ray);
}
