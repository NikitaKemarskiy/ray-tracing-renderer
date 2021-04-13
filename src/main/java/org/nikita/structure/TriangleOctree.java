package org.nikita.structure;

import org.nikita.geometry.Triangle;
import org.nikita.geometry.Vector;

public class TriangleOctree implements TriangleTree {
    private TriangleBoundingBox root;

    public TriangleOctree(Vector vertex1, Vector vertex2, int depth) {
        root = new TriangleBoundingBox(vertex1, vertex2, depth);
    }

    public void addTriangle(Triangle triangle) {
        root.addTriangle(triangle);
    }

    @Override
    public String toString() {
        return "Octree{" +
                "root=" + root +
                '}';
    }
}
