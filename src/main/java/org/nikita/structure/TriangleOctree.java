package org.nikita.structure;

import org.nikita.calculation.MollerTrumboreSolver;
import org.nikita.calculation.RayTriangleIntersectionSolver;
import org.nikita.geometry.Triangle;
import org.nikita.geometry.Vector;

import java.util.Set;

public class TriangleOctree implements TriangleTree {
    private TriangleBoundingBox root;
    private RayTriangleIntersectionSolver rayTriangleIntersectionSolver;

    public TriangleOctree(Vector vertex1, Vector vertex2, int depth) {
        root = new TriangleBoundingBox(vertex1, vertex2, depth);
        rayTriangleIntersectionSolver = new MollerTrumboreSolver();
    }

    public void addTriangle(Triangle triangle) {
        root.addTriangle(triangle);
    }

    public double getTriangleIntersectionDistanceWithRay(Vector from, Vector ray) {
        return root.getTriangleIntersectionDistanceWithRay(from, ray, rayTriangleIntersectionSolver);
    }

    @Override
    public String toString() {
        return "Octree{" +
                "root=" + root +
                '}';
    }
}
