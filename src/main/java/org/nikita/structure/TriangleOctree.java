package org.nikita.structure;

import org.nikita.calculation.RayTriangleIntersectionSolver;
import org.nikita.geometry.Ray;
import org.nikita.geometry.Triangle;
import org.nikita.renderer.TriangleIntersection;

public class TriangleOctree implements TriangleTree {

    private TriangleBoundingBox root;
    private RayTriangleIntersectionSolver rayTriangleIntersectionSolver;

    public TriangleOctree(TriangleBoundingBox root, RayTriangleIntersectionSolver rayTriangleIntersectionSolver) {
        this.root = root;
        this.rayTriangleIntersectionSolver = rayTriangleIntersectionSolver;
    }

    @Override
    public void addTriangle(Triangle triangle) {
        root.addTriangle(triangle);
    }

    @Override
    public TriangleIntersection getTriangleIntersectionWithRay(Ray ray) {
        return root.getTriangleIntersectionWithRay(ray, rayTriangleIntersectionSolver);
    }

    @Override
    public TriangleBoundingBox getRoot() {
        return root;
    }

    @Override
    public String toString() {
        return "Octree{" +
                "root=" + root +
                '}';
    }
}
