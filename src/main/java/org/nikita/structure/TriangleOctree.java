package org.nikita.structure;

import org.nikita.geometry.Triangle;
import org.nikita.geometry.Vector;

import java.util.Set;

public class TriangleOctree implements TriangleTree {
    private TriangleBoundingBox root;

    public TriangleOctree(Vector vertex1, Vector vertex2, int depth) {
        root = new TriangleBoundingBox(vertex1, vertex2, depth);
    }

    public void addTriangle(Triangle triangle) {
        root.addTriangle(triangle);
    }

    public Set<Triangle> getTrianglesByRay(Vector from, Vector ray) {
        return root.getTrianglesByRay(from, ray);
    }

    @Override
    public String toString() {
        return "Octree{" +
                "root=" + root +
                '}';
    }
}
