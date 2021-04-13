package org.nikita.geometry;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Triangle {
    private final static int MAX_VERTICES = 3;

    private List<Vector> vertices;

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
            .map(vertex -> String.format(vertex.toString()))
            .collect(Collectors.joining(", "));
    }
}
