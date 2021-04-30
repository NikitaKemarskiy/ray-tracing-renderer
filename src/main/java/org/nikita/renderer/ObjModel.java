package org.nikita.renderer;

import de.javagl.obj.*;
import org.nikita.calculation.TriangleColorIntensitySolver;
import org.nikita.geometry.Vector;
import org.nikita.geometry.*;
import org.nikita.structure.TriangleTree;
import org.nikita.util.MapUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

public class ObjModel {

    private Set<Triangle> triangles;
    private Color objectColor;
    private Color backgroundColor;
    private TriangleColorIntensitySolver triangleColorIntensitySolver;
    private TriangleTree triangleTree;

    public ObjModel(Color objectColor, Color backgroundColor,
                    TriangleColorIntensitySolver triangleColorIntensitySolver, TriangleTree triangleTree) {
        this.objectColor = objectColor;
        this.backgroundColor = backgroundColor;
        this.triangleColorIntensitySolver = triangleColorIntensitySolver;
        this.triangleTree = triangleTree;
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

        triangleTree.getRoot().setVertex1(minCoordinates);
        triangleTree.getRoot().setVertex2(maxCoordinates);
        triangleTree.getRoot().setTriangles(new HashSet<>());
        triangleTree.getRoot().initChildren(2);

        for (Triangle triangle : triangles) {
            triangleTree.addTriangle(triangle);
        }
    }

    private void buildVerticesNormals(
    ) {
        Map<Integer, List<Vector>> vertexCoordinatesVerticesMap = new HashMap<>();
        Map<Integer, List<Triangle>> vertexCoordinatesTrianglesMap = new HashMap<>();

        for (Triangle triangle : triangles) {
            for (Vector vertex : triangle.getVertices()) {
                MapUtil.addToList(vertexCoordinatesVerticesMap, vertex.hashCode(), vertex);
                MapUtil.addToList(vertexCoordinatesTrianglesMap, vertex.hashCode(), triangle);
            }
        }

        for (Map.Entry<Integer, List<Vector>> entry : vertexCoordinatesVerticesMap.entrySet()) {
            int vertexHash = entry.getKey();
            List<Vector> vertices = entry.getValue();

            List<Triangle> triangles = vertexCoordinatesTrianglesMap.get(vertexHash);

            Vector vertexNormal = new Vector(0, 0, 0);

            for (Triangle triangle : triangles) {
                vertexNormal = vertexNormal.add(triangle.getNormal());
            }

            vertexNormal = vertexNormal.divide(triangles.size());

            for (Vector vertex : vertices) {
                triangleColorIntensitySolver.addVerticesNormals(vertex, vertexNormal);
            }
        }
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
                }
            }
        }
    }

    public void init() {
        buildTree();
        buildVerticesNormals();
    }

    public Color getColor() {
        return objectColor;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
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
