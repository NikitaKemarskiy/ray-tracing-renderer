package org.nikita.structure;

import org.nikita.geometry.Triangle;
import org.nikita.geometry.Vector;

import java.util.Set;

public interface TriangleTree {
    void addTriangle(Triangle triangle);
    Set<Triangle> getTrianglesByRay(Vector from, Vector ray);
}
