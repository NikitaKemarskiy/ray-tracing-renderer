package org.nikita.renderer;

import de.javagl.obj.*;
import org.nikita.geometry.Polygon;
import org.nikita.geometry.Vector;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ObjModel {
    private Set<Polygon> polygons;

    private void updateMinCoordinates(Vector minCoordinates, Vector vertex) {
        minCoordinates.setX(
            minCoordinates.getX() < vertex.getX()
                ? minCoordinates.getX()
                : vertex.getX()
        );
        minCoordinates.setY(
                minCoordinates.getY() < vertex.getY()
                    ? minCoordinates.getY()
                    : vertex.getY()
        );
        minCoordinates.setZ(
                minCoordinates.getZ() < vertex.getZ()
                    ? minCoordinates.getZ()
                    : vertex.getZ()
        );
    }

    private void updatePolygonsPosition(Vector minCoordinates, Vector position) {
        double xDiff = position.getX() - minCoordinates.getX();
        double yDiff = position.getY() - minCoordinates.getY();
        double zDiff = position.getZ() - minCoordinates.getZ();
        for (Polygon polygon : polygons) {
            for (Vector vertex : polygon.getVertices()) {
                vertex.setX(vertex.getX() + xDiff);
                vertex.setX(vertex.getY() + yDiff);
                vertex.setX(vertex.getZ() + zDiff);
            }
        }
    }

    public ObjModel(String source, Vector position) throws IOException {
        polygons = new HashSet<>();

        try (InputStream inputStream = new FileInputStream(source)) {
            Obj obj = ObjUtils.convertToRenderable(ObjReader.read(inputStream));
            Vector minCoordinates = new Vector(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);

            for (int faceIndex = 0; faceIndex < obj.getNumFaces(); faceIndex++) {
                ObjFace face = obj.getFace(faceIndex);
                Polygon polygon = new Polygon();
                for (int vertexNumber = 0; vertexNumber < face.getNumVertices(); vertexNumber++) {
                    FloatTuple floatTuple = obj.getVertex(face.getVertexIndex(vertexNumber));
                    Vector vector = new Vector(
                        floatTuple.getX(),
                        floatTuple.getY(),
                        floatTuple.getZ()
                    );
                    updateMinCoordinates(minCoordinates, vector);
                    polygon.addVertex(vector);
                }
                polygons.add(polygon);
            }

            updatePolygonsPosition(minCoordinates, position);
        }
    }

    public Set<Polygon> getPolygons() {
        return polygons;
    }

    public void setPolygons(Set<Polygon> polygons) {
        this.polygons = polygons;
    }

    @Override
    public String toString() {
        return String.format(
            "Polygons: [%s]",
            polygons.stream()
                .map(polygon -> String.format("{ %s }", polygon.toString()))
                .collect(Collectors.joining(", "))
        );
    }
}
