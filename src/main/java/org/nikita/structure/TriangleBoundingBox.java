package org.nikita.structure;

import org.nikita.geometry.Triangle;
import org.nikita.geometry.Vector;

import java.util.*;

public class TriangleBoundingBox {
    private final static int CHILDREN_AMOUNT = 8;
    private final static int AXIS_DIVISION_PARTS = 2;

    private Vector vertex1;
    private Vector vertex2;
    private Set<Triangle> triangles;
    private List<TriangleBoundingBox> children;

    private boolean vertexBelongsTo(Vector vertex) {
        return
            vertex.getX() >= Math.min(vertex1.getX(), vertex2.getX()) &&
            vertex.getX() <= Math.max(vertex1.getX(), vertex2.getX()) &&
            vertex.getY() >= Math.min(vertex1.getY(), vertex2.getY()) &&
            vertex.getY() <= Math.max(vertex1.getY(), vertex2.getY()) &&
            vertex.getZ() >= Math.min(vertex1.getZ(), vertex2.getZ()) &&
            vertex.getZ() <= Math.max(vertex1.getZ(), vertex2.getZ());
    }

    private TriangleBoundingBox getChild(Vector vertex) {
        for (TriangleBoundingBox child : children) {
            if (child.vertexBelongsTo(vertex)) {
                return child;
            }
        }

        return null;
    }

    private void initChildren(int depth) {
        children = new ArrayList<>(CHILDREN_AMOUNT);

        if (depth != 0) {
            Vector vertexStep = vertex2
                .subtract(vertex1)
                .divide(
                    new Vector(
                        AXIS_DIVISION_PARTS,
                        AXIS_DIVISION_PARTS,
                        AXIS_DIVISION_PARTS
                    )
                );

            for (double x = vertex1.getX(); x < vertex2.getX(); x += vertexStep.getX()) {
                for (double y = vertex1.getY(); y < vertex2.getY(); y += vertexStep.getY()) {
                    for (double z = vertex1.getZ(); z < vertex2.getZ(); z += vertexStep.getZ()) {
                        Vector vertex1 = new Vector(x, y, z);
                        Vector vertex2 = vertex1.add(vertexStep);
                        children.add(new TriangleBoundingBox(vertex1, vertex2, depth));
                    }
                }
            }
        }
    }

    public TriangleBoundingBox(Vector vertex1, Vector vertex2, int depth) {
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        triangles = new HashSet<>();
        initChildren(depth - 1);
    }

    public void addTriangle(Triangle triangle) {
        Iterator<Vector> verticesIterator = triangle.getVertices().iterator();
        boolean triangleBelongsTo = false;

        while (verticesIterator.hasNext()) {
            Vector vertex = verticesIterator.next();
            boolean vertexBelongsTo = vertexBelongsTo(vertex);
            triangleBelongsTo = triangleBelongsTo || vertexBelongsTo;

            if (vertexBelongsTo) {
                TriangleBoundingBox child = getChild(vertex);
                if (child != null) {
                    child.addTriangle(triangle);
                }
            }
        }

        if (triangleBelongsTo) {
            triangles.add(triangle);
        }
    }

    @Override
    public String toString() {
        return "BoundingBox{" +
                "vertex1=" + vertex1 +
                ", vertex2=" + vertex2 +
                ", triangles=" + triangles +
                ", children=" + children +
                '}';
    }
}