package org.nikita.geometry;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Polygon {
    Set<Vector> vertices;

    public Polygon() {
        this.vertices = new HashSet<>();
    }

    public Polygon(Vector[] vertices) {
        this.vertices = new HashSet<>(Arrays.asList(vertices));
    }

    public void addVertex(Vector vertex) {
        vertices.add(vertex);
    }

    public Set<Vector> getVertices() {
        return vertices;
    }

    public void setVertices(Set<Vector> vertices) {
        this.vertices = vertices;
    }

    @Override
    public String toString() {
        return vertices.stream()
            .map(vertex -> String.format(vertex.toString()))
            .collect(Collectors.joining(", "));
    }
}
