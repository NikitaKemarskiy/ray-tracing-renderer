package org.nikita.renderer;

import com.paulok777.formats.Image;
import com.paulok777.formats.Pixel;
import org.nikita.calculation.MollerTrumboreSolver;
import org.nikita.calculation.RayTriangleIntersectionSolver;
import org.nikita.geometry.Axis;
import org.nikita.geometry.Triangle;
import org.nikita.geometry.Vector;
import org.nikita.structure.TriangleTree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.nikita.formats.Color.BLACK;
import static org.nikita.formats.Color.WHITE;

public class RayTracingObjRenderer implements Renderer {
    private final static int DEFAULT_MODEL_MIN_POSITION = 2;
    private final static Vector DEFAULT_CAMERA_POSITION = Vector.ZERO;
    private final static Vector DEFAULT_SCREEN_CENTER = new Vector(0, 0, 1);
    private final static Axis DEFAULT_SCREEN_WIDTH_AXIS = Axis.X;
    private final static Axis DEFAULT_SCREEN_HEIGHT_AXIS = Axis.Y;
    private final static Axis DEFAULT_SCREEN_NORMAL_AXIS = Axis.Z;
    private final static int DEFAULT_SCREEN_PIXEL_WIDTH = 600;
    private final static int DEFAULT_SCREEN_PIXEL_HEIGHT = 600;

    private RayTriangleIntersectionSolver rayTriangleIntersectionSolver;

    private boolean hasIntersection(Vector cameraPosition, Vector ray, ObjModel objModel) {
        for (Triangle triangle : objModel.getTriangles()) {
            if (rayTriangleIntersectionSolver.intersects(cameraPosition, ray, triangle)) {
                return true;
            }
        }
        return false;
    }

    private Image renderImageFromObjModel(ObjModel objModel, Screen screen, Vector cameraPosition) {
        Image image = new Image();

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

        List<Pixel> pixels = new ArrayList<>();

        for (double h = screenMinByHeightAxis; h < screenMaxByHeightAxis; h += screenHeightStep) {
            for (double w = screenMinByWidthAxis; w < screenMaxByWidthAxis; w += screenWidthStep) {
                Vector ray = Vector.ZERO;

                ray.setCoordinateValue(w, screenWidthAxis);
                ray.setCoordinateValue(h, screenHeightAxis);
                ray.setCoordinateValue(screenPositionByNormalAxis, screenNormalAxis);

                Pixel pixel = hasIntersection(cameraPosition, ray, objModel) ? BLACK : WHITE;
                pixels.add(pixel);
            }
        }

        image.setWidth(screen.getPixelWidth());
        image.setHeight(screen.getPixelHeight());
        image.setPixels(pixels.toArray(new Pixel[0]));

        return image;
    }

    public RayTracingObjRenderer() {
        rayTriangleIntersectionSolver = new MollerTrumboreSolver();
    }

    @Override
    public Image render(String source) throws IOException {
        ObjModel objModel = new ObjModel(source);
        Screen screen = new Screen(
            DEFAULT_SCREEN_CENTER,
            DEFAULT_SCREEN_WIDTH_AXIS,
            DEFAULT_SCREEN_HEIGHT_AXIS,
            DEFAULT_SCREEN_NORMAL_AXIS,
            DEFAULT_SCREEN_PIXEL_WIDTH,
            DEFAULT_SCREEN_PIXEL_HEIGHT
        );

        objModel.setMin(DEFAULT_MODEL_MIN_POSITION, DEFAULT_SCREEN_NORMAL_AXIS);
        TriangleTree triangleTree = objModel.buildTree();

        return renderImageFromObjModel(objModel, screen, DEFAULT_CAMERA_POSITION);
    }
}
