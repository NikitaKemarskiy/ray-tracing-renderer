package org.nikita.renderer;

import com.paulok777.formats.Image;
import com.paulok777.formats.Pixel;
import org.nikita.calculation.MollerTrumboreSolver;
import org.nikita.calculation.RayTriangleIntersectionSolver;
import org.nikita.geometry.Axis;
import org.nikita.geometry.Triangle;
import org.nikita.geometry.Vector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.nikita.formats.Color.BLACK;
import static org.nikita.formats.Color.WHITE;


public class RayTracingObjRenderer implements Renderer {
    private final static Vector DEFAULT_MODEL_POSITION = new Vector(0, 0, 2);
    private final static Vector DEFAULT_CAMERA_POSITION = Vector.ZERO;
    private final static Vector DEFAULT_SCREEN_CENTER = new Vector(0, 0, 1);
    private final static Axis DEFAULT_SCREEN_AXIS_PARALLEL_TO = Axis.X;
    private final static Axis DEFAULT_SCREEN_AXIS_NORMAL_TO = Axis.Z;
    private final static int DEFAULT_SCREEN_WIDTH = 50;
    private final static int DEFAULT_SCREEN_HEIGHT = 50;

    private RayTriangleIntersectionSolver rayTriangleIntersectionSolver;

    private boolean hasIntersection(Vector ray, ObjModel objModel) {
        for (Triangle triangle : objModel.getTriangles()) {
            if (rayTriangleIntersectionSolver.intersects(ray, triangle)) {
                return true;
            }
        }
        return false;
    }

    private Image renderImageFromObjModel(ObjModel objModel, Camera camera, Screen screen) {
        Image image = new Image();
        image.setWidth(screen.getWidth());
        image.setHeight(screen.getHeight());

        double screenMinX = screen.getMin(Axis.X);
        double screenMaxX = screen.getMax(Axis.X);
        double screenMinY = screen.getMin(Axis.Y);
        double screenMaxY = screen.getMax(Axis.Y);
        double screenMinZ = screen.getMin(Axis.Z);
        double screenMaxZ = screen.getMax(Axis.Z);

        List<Pixel> pixels = new ArrayList<>();
        System.out.println(screenMinX + ":" + screenMaxX);
        System.out.println(screenMinY + ":" + screenMaxY);
        System.out.println(screenMinZ + ":" + screenMaxZ);
        for (double x = screenMinX; x <= screenMaxX; x++) {
            for (double y = screenMinY; y <= screenMaxY; y++) {
                for (double z = screenMinZ; z <= screenMaxZ; z++) {
                    Vector ray = new Vector(x, y, z).subtract(camera.getPosition());

                    Pixel pixel = hasIntersection(ray, objModel) ? BLACK : WHITE;
                    pixels.add(pixel);
                }
            }
        }

        image.setPixels(pixels.toArray(new Pixel[0]));
        return image;
    }

    public RayTracingObjRenderer() {
        rayTriangleIntersectionSolver = new MollerTrumboreSolver();
    }

    @Override
    public Image render(String source) throws IOException {
        ObjModel objModel = new ObjModel(source, DEFAULT_MODEL_POSITION);
        Camera camera = new Camera(DEFAULT_CAMERA_POSITION);
        Screen screen = new Screen(
            DEFAULT_SCREEN_CENTER,
            DEFAULT_SCREEN_AXIS_PARALLEL_TO,
            DEFAULT_SCREEN_AXIS_NORMAL_TO,
            DEFAULT_SCREEN_WIDTH,
            DEFAULT_SCREEN_HEIGHT
        );

        return renderImageFromObjModel(objModel, camera, screen);
    }
}
