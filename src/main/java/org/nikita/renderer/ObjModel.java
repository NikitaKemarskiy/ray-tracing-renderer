package org.nikita.renderer;

import de.javagl.obj.*;
import org.nikita.geometry.Vector;
import org.nikita.geometry.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

public class ObjModel {

    private Set<Triangle> triangles;
    private Color objectColor;

    public ObjModel(Color objectColor) {
        this.objectColor = objectColor;
        this.triangles = new HashSet<>();
    }

    public void populateTriangles(InputStream inputStream) throws IOException {
        Obj obj = ObjUtils.convertToRenderable(ObjReader.read(inputStream));

        for (int faceIndex = 0; faceIndex < obj.getNumFaces(); faceIndex++) {
            ObjFace face = obj.getFace(faceIndex);
            Triangle triangle = new Triangle();
            for (int vertexNumber = 0; vertexNumber < face.getNumVertices(); vertexNumber++) {
                int vertexIndex = face.getVertexIndex(vertexNumber);
                FloatTuple floatTuple = obj.getVertex(vertexIndex);

                Vector vertex = new Vector(
                        floatTuple.getX(),
                        floatTuple.getY(),
                        floatTuple.getZ()
                );

                triangle.addVertex(vertex);
            }
            triangles.add(triangle);
        }
    }

    public Vector getMinCoordinates() {
        Vector minCoordinates = new Vector(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);

        for (Triangle triangle : triangles) {
            for (Vector vertex : triangle.getVertices()) {
                minCoordinates.setX(Math.min(minCoordinates.getX(), vertex.getX()));
                minCoordinates.setY(Math.min(minCoordinates.getY(), vertex.getY()));
                minCoordinates.setZ(Math.min(minCoordinates.getZ(), vertex.getZ()));
            }
        }

        return minCoordinates;
    }

    public Vector getMaxCoordinates() {
        Vector maxCoordinates = new Vector(Double.MIN_VALUE, Double.MIN_VALUE, Double.MIN_VALUE);

        for (Triangle triangle : triangles) {
            for (Vector vertex : triangle.getVertices()) {
                maxCoordinates.setX(Math.max(maxCoordinates.getX(), vertex.getX()));
                maxCoordinates.setY(Math.max(maxCoordinates.getY(), vertex.getY()));
                maxCoordinates.setZ(Math.max(maxCoordinates.getZ(), vertex.getZ()));
            }
        }

        return maxCoordinates;
    }
    
    public void setMin(double minValue, Axis axis) {
        Vector minCoordinates = getMinCoordinates();

        double diff = minValue - switch (axis) {
            case X -> minCoordinates.getX();
            case Y -> minCoordinates.getY();
            case Z -> minCoordinates.getZ();
        };

        for (Triangle triangle : triangles) {
            for (Vector vertex : triangle.getVertices()) {
                switch (axis) {
                    case X -> vertex.setX(vertex.getX() + diff);
                    case Y -> vertex.setY(vertex.getY() + diff);
                    case Z -> vertex.setZ(vertex.getZ() + diff);
                }
            }
        }
    }

    public Color getColor() {
        return objectColor;
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
