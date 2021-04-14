package org.nikita.structure;

import org.nikita.geometry.Ray;
import org.nikita.geometry.Triangle;
import org.nikita.renderer.TriangleIntersection;

public interface TriangleTree {

    void addTriangle(Triangle triangle);

    TriangleIntersection getTriangleIntersectionWithRay(Ray ray);
}
