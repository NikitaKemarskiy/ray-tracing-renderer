package org.nikita.renderer;

import de.javagl.obj.*;
import org.nikita.geometry.Triangle;
import org.nikita.geometry.Vector;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ObjModel {
    private Set<Triangle> triangles;

    private void updateMinCoordinates(Vector minCoordinates, Vector vertex) {
        minCoordinates.setX(Math.min(minCoordinates.getX(), vertex.getX()));
        minCoordinates.setY(Math.min(minCoordinates.getY(), vertex.getY()));
        minCoordinates.setZ(Math.min(minCoordinates.getZ(), vertex.getZ()));
    }

    private void updatePolygonsPosition(Vector minCoordinates, Vector position) {
        double xDiff = position.getX() - minCoordinates.getX();
        double yDiff = position.getY() - minCoordinates.getY();
        double zDiff = position.getZ() - minCoordinates.getZ();
        for (Triangle triangle : triangles) {
            for (Vector vertex : triangle.getVertices()) {
                vertex.setX(vertex.getX() + xDiff);
                vertex.setX(vertex.getY() + yDiff);
                vertex.setX(vertex.getZ() + zDiff);
            }
        }
    }

    public ObjModel(String source, Vector position) throws IOException {
        triangles = new HashSet<>();

        try (InputStream inputStream = new FileInputStream(source)) {
            Obj obj = ObjUtils.convertToRenderable(ObjReader.read(inputStream));
            Vector minCoordinates = new Vector(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);

            for (int faceIndex = 0; faceIndex < obj.getNumFaces(); faceIndex++) {
                ObjFace face = obj.getFace(faceIndex);
                Triangle triangle = new Triangle();
                for (int vertexNumber = 0; vertexNumber < face.getNumVertices(); vertexNumber++) {
                    FloatTuple floatTuple = obj.getVertex(face.getVertexIndex(vertexNumber));
                    Vector vector = new Vector(
                        floatTuple.getX(),
                        floatTuple.getY(),
                        floatTuple.getZ()
                    );
                    updateMinCoordinates(minCoordinates, vector);
                    triangle.addVertex(vector);
                }
                triangles.add(triangle);
            }

            updatePolygonsPosition(minCoordinates, position);
        }
    }

    public Set<Triangle> getTriangles() {
        return triangles;
    }

    @Override
    public String toString() {
        return String.format(
            "Polygons: [%s]",
            triangles.stream()
                .map(triangle -> String.format("{ %s }", triangle.toString()))
                .collect(Collectors.joining(", "))
        );
    }
}
