package org.nikita.renderer;

import com.paulok777.formats.Image;
import com.paulok777.formats.Pixel;
import org.nikita.calculation.TriangleColorIntensitySolver;
import org.nikita.geometry.*;
import org.nikita.geometry.Vector;
import org.nikita.structure.TriangleTree;
import org.nikita.util.MapUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class RayTracingObjRenderer implements Renderer {

    private double modelMinPosition;
    private Vector camera;
    private ObjModel objModel;
    private Screen screen;
    private TriangleTree triangleTree;
    private TriangleColorIntensitySolver triangleColorIntensitySolver;

    public RayTracingObjRenderer(
        double modelMinPosition,
        Vector camera,
        ObjModel objModel,
        Screen screen,
        TriangleTree triangleTree,
        TriangleColorIntensitySolver triangleColorIntensitySolver
    ) {
        this.modelMinPosition = modelMinPosition;
        this.camera = camera;
        this.objModel = objModel;
        this.screen = screen;
        this.triangleTree = triangleTree;
        this.triangleColorIntensitySolver = triangleColorIntensitySolver;
    }

    @Override
    public Image render(InputStream inputStream) throws IOException {
        objModel.populateTriangles(inputStream);
        objModel.setMin(modelMinPosition, screen.getNormalAxis());

        buildVerticesNormals(objModel);
        buildTree(objModel);

        return renderImageFromObjModel(objModel, screen, camera);
    }

    private void buildTree(ObjModel objModel) {
        Vector minCoordinates = objModel.getMinCoordinates();
        Vector maxCoordinates = objModel.getMaxCoordinates();

        triangleTree.getRoot().setVertex1(minCoordinates);
        triangleTree.getRoot().setVertex2(maxCoordinates);
        triangleTree.getRoot().setTriangles(new HashSet<>());
        triangleTree.getRoot().initChildren(2);

        for (Triangle triangle : objModel.getTriangles()) {
            triangleTree.addTriangle(triangle);
        }
    }

    private void buildVerticesNormals(ObjModel objModel) {
        Map<Integer, List<Vector>> vertexCoordinatesVerticesMap = new HashMap<>();
        Map<Integer, List<Triangle>> vertexCoordinatesTrianglesMap = new HashMap<>();

        for (Triangle triangle : objModel.getTriangles()) {
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

    private double getColorIntensity(ObjModel objModel, Ray ray) {
        TriangleIntersection triangleIntersectionWithRay = triangleTree.getTriangleIntersectionWithRay(ray);

        if (triangleIntersectionWithRay == null) {
            return 0;
        }

        return triangleColorIntensitySolver.getTrianglePointColorIntensity(
                triangleIntersectionWithRay.getTriangle(),
                triangleIntersectionWithRay.getPoint()
        );
    }

    private Image renderImageFromObjModel(ObjModel objModel, Screen screen, Vector cameraPosition) {
        Axis screenNormalAxis = screen.getNormalAxis();
        Axis screenWidthAxis = screen.getWidthAxis();
        Axis screenHeightAxis = screen.getHeightAxis();

        double screenWidthStep = screen.getWidthStep();
        double screenHeightStep = screen.getHeightStep();

        double screenPositionByNormalAxis = screen.getCenter().getCoordinateValue(screenNormalAxis);
        double screenMinByWidthAxis = screen.getMin(screenWidthAxis);
        double screenMaxByWidthAxis = screen.getMax(screenWidthAxis);
        double screenMinByHeightAxis = screen.getMin(screenHeightAxis);
        double screenMaxByHeightAxis = screen.getMax(screenHeightAxis);

        Color objModelColor = objModel.getColor();

        List<Pixel> pixels = new ArrayList<>();

        for (double h = screenMaxByHeightAxis; h > screenMinByHeightAxis; h -= screenHeightStep) {
            for (double w = screenMinByWidthAxis; w < screenMaxByWidthAxis; w += screenWidthStep) {
                Vector direction = new Vector(0, 0, 0);

                direction.setCoordinateValue(w, screenWidthAxis);
                direction.setCoordinateValue(h, screenHeightAxis);
                direction.setCoordinateValue(screenPositionByNormalAxis, screenNormalAxis);

                Ray ray = new Ray(cameraPosition, direction);

                double colorIntensity = getColorIntensity(objModel, ray);

                Color color;
                if (colorIntensity > 0) {
                    color = objModelColor.multiply(colorIntensity);
                } else {
                    color = objModel.getBackgroundColor();
                }

                Pixel pixel = new Pixel((byte) color.getRed(), (byte) color.getGreen(), (byte) color.getBlue());
                pixels.add(pixel);
            }
        }

        Image image = new Image();

        image.setWidth(screen.getPixelWidth());
        image.setHeight(screen.getPixelHeight());
        image.setPixels(pixels.toArray(new Pixel[0]));

        return image;
    }
}
