package org.nikita.calculation;

import org.nikita.geometry.Triangle;
import org.nikita.geometry.Vector;

public interface TriangleColorIntensitySolver {

    double getTrianglePointColorIntensity(Triangle triangle, Vector point);

    void addVerticesNormals(Vector vertex, Vector vertexNormal);
}
