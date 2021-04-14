package org.nikita.geometry;

import java.util.*;
import java.util.stream.Collectors;

public class Triangle {

    private final static int MAX_VERTICES = 3;

    private List<Vector> vertices;
    private Vector normal;

    private Vector calculateNormal() {
        Iterator<Vector> verticesIterator = vertices.iterator();

        Vector vertex0 = verticesIterator.next();
        Vector vertex1 = verticesIterator.next();
        Vector vertex2 = verticesIterator.next();

        Vector edge1 = vertex1.subtract(vertex0);
        Vector edge2 = vertex2.subtract(vertex0);

        normal = edge1.crossProduct(edge2);

        return normal;
    }

    private boolean isVerticesNumberCorrect(int verticesNumber) {
        return verticesNumber <= MAX_VERTICES;
    }

    public Triangle() {
        this.vertices = new ArrayList<>(3);
    }

    public void addVertex(Vector vertex) {
        if (!isVerticesNumberCorrect(vertices.size() + 1)) {
            throw new IndexOutOfBoundsException("Vertices number exceeded");
        }

        vertices.add(vertex);
    }

    public Vector getNormal() {
        return Optional.ofNullable(normal).orElse(calculateNormal());
    }

    public Vector getCentroid() {
        Vector centroid = new Vector(0, 0, 0);

        for (Vector vertex : vertices) {
            centroid = centroid.add(vertex);
        }

        return centroid.divide(vertices.size());
    }

    public List<Vector> getVertices() {
        return vertices;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Triangle triangle = (Triangle) o;
        return Objects.equals(vertices, triangle.vertices);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vertices);
    }

    @Override
    public String toString() {
        return vertices.stream()
            .map(Vector::toString)
            .collect(Collectors.joining(", "));
    }
}
