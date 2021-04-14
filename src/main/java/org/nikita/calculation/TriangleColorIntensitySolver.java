package org.nikita.calculation;

import org.nikita.geometry.Triangle;
import org.nikita.geometry.Vector;

public interface TriangleColorIntensitySolver {
    double getTriangleColorIntensity(Triangle triangle, Vector lightSourcePosition);
}
