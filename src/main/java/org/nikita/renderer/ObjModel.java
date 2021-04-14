package org.nikita.renderer;

import de.javagl.obj.*;
import org.nikita.calculation.NormalTriangleColorIntensitySolver;
import org.nikita.calculation.TriangleColorIntensitySolver;
import org.nikita.geometry.*;
import org.nikita.structure.TriangleOctree;
import org.nikita.structure.TriangleTree;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ObjModel {
    private Set<Triangle> triangles;
    private TriangleTree triangleTree;
    private Color color;
    private TriangleColorIntensitySolver triangleColorIntensitySolver;

    private Vector getMinCoordinates() {
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

    private Vector getMaxCoordinates() {
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

    private void buildTree() {
        Vector minCoordinates = getMinCoordinates();
        Vector maxCoordinates = getMaxCoordinates();

        triangleTree = new TriangleOctree(minCoordinates, maxCoordinates, 3);

        for (Triangle triangle : triangles) {
            triangleTree.addTriangle(triangle);
        }
    }

    public ObjModel(
        String source,
        Color color,
        Vector lightSourcePosition,
        double ambientLightIntensity
    ) throws IOException {
        this.color = color;

        triangles = new HashSet<>();
        triangleColorIntensitySolver = new NormalTriangleColorIntensitySolver(ambientLightIntensity, lightSourcePosition);

        try (InputStream inputStream = new FileInputStream(source)) {
            Obj obj = ObjUtils.convertToRenderable(ObjReader.read(inputStream));

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
                    triangle.addVertex(vector);
                }
                triangles.add(triangle);
            }
        }

        buildTree();
    }
    
    public double getColorIntensity(Ray ray) {
        TriangleIntersection triangleIntersectionWithRay = triangleTree.getTriangleIntersectionWithRay(ray);

        if (triangleIntersectionWithRay == null) {
            return 0;
        }

        return triangleColorIntensitySolver.getTrianglePointColorIntensity(
            triangleIntersectionWithRay.getTriangle(),
            triangleIntersectionWithRay.getPoint()
        );
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
                };
            }
        }

        buildTree();
    }

    public Color getColor() {
        return color;
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
