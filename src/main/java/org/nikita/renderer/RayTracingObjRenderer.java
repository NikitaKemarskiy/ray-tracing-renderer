package org.nikita.renderer;

import org.nikita.formats.Image;
import org.nikita.geometry.Axis;
import org.nikita.geometry.Polygon;
import org.nikita.geometry.Ray;
import org.nikita.geometry.Vector;

import java.io.IOException;

public class RayTracingObjRenderer implements Renderer {
    private final static Vector DEFAULT_MODEL_POSITION = new Vector(0, 0, 100);
    private final static Vector DEFAULT_CAMERA_POSITION = Vector.ZERO;
    private final static Vector DEFAULT_SCREEN_CENTER = new Vector(0, 0, 1);
    private final static Axis DEFAULT_SCREEN_AXIS_PARALLEL_TO = Axis.X;
    private final static Axis DEFAULT_SCREEN_AXIS_NORMAL_TO = Axis.Z;
    private final static int DEFAULT_SCREEN_WIDTH = 800;
    private final static int DEFAULT_SCREEN_HEIGHT = 600;

    private Image renderImageFromObjModel(ObjModel objModel, Camera camera, Screen screen) {
        Image image = new Image(screen.getWidth(), screen.getHeight());

        double screenMinX = screen.getMin(Axis.X);
        double screenMaxX = screen.getMax(Axis.X);
        double screenMinY = screen.getMin(Axis.Y);
        double screenMaxY = screen.getMax(Axis.Y);
        double screenMinZ = screen.getMin(Axis.Z);
        double screenMaxZ = screen.getMax(Axis.Z);

        for (double x = screenMinX; x <= screenMaxX; x++) {
            for (double y = screenMinY; y <= screenMaxY; y++) {
                for (double z = screenMinZ; z <= screenMaxZ; z++) {
                    Ray ray = new Ray(camera.getPosition(), new Vector(x, y, z));

                    System.out.println(new Vector(x, y, z));

                    for (Polygon polygon : objModel.getPolygons()) {
                        //
                    }
                }
            }
        }

        return image;
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
