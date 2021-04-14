package org.nikita.renderer;

import com.paulok777.formats.Image;
import com.paulok777.formats.Pixel;
import org.nikita.geometry.Axis;
import org.nikita.geometry.Color;
import org.nikita.geometry.Vector;
import org.nikita.geometry.Ray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RayTracingObjRenderer implements Renderer {
    private final static double DEFAULT_MODEL_MIN_POSITION = 1.5;
    private final static Vector DEFAULT_CAMERA_POSITION = new Vector(0, 0, 0);
    private final static Vector DEFAULT_LIGHT_SOURCE_POSITION = new Vector(-1, 0, -1);
    private final static Vector DEFAULT_SCREEN_CENTER = new Vector(0, 1, 0);
    private final static Axis DEFAULT_SCREEN_WIDTH_AXIS = Axis.X;
    private final static Axis DEFAULT_SCREEN_HEIGHT_AXIS = Axis.Z;
    private final static Axis DEFAULT_SCREEN_NORMAL_AXIS = Axis.Y;
    private final static int DEFAULT_SCREEN_PIXEL_WIDTH = 1000;
    private final static int DEFAULT_SCREEN_PIXEL_HEIGHT = 1000;
    private final static Color DEFAULT_OBJECT_COLOR = new Color(255, 255, 0);
    private final static Color DEFAULT_BACKGROUND_COLOR = Color.BLACK;
    private final static double DEFAULT_AMBIENT_LIGHT_INTENSITY = 0.1;

    private Image renderImageFromObjModel(
        ObjModel objModel,
        Screen screen,
        Vector cameraPosition
    ) {
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

        Color objModelColor = objModel.getColor();

        List<Pixel> pixels = new ArrayList<>();

        for (double h = screenMaxByHeightAxis; h > screenMinByHeightAxis; h -= screenHeightStep) {
            for (double w = screenMinByWidthAxis; w < screenMaxByWidthAxis; w += screenWidthStep) {
                Vector direction = new Vector(0, 0, 0);

                direction.setCoordinateValue(w, screenWidthAxis);
                direction.setCoordinateValue(h, screenHeightAxis);
                direction.setCoordinateValue(screenPositionByNormalAxis, screenNormalAxis);

                Ray ray = new Ray(cameraPosition, direction);

                double colorIntensity = objModel.getColorIntensity(ray);

                Color color = colorIntensity > 0
                    ? objModelColor.multiply(colorIntensity)
                    : DEFAULT_BACKGROUND_COLOR;

                Pixel pixel = new Pixel((byte) color.getRed(), (byte) color.getGreen(), (byte) color.getBlue());
                pixels.add(pixel);
            }
        }

        image.setWidth(screen.getPixelWidth());
        image.setHeight(screen.getPixelHeight());
        image.setPixels(pixels.toArray(new Pixel[0]));

        return image;
    }

    @Override
    public Image render(String source) throws IOException {
        ObjModel objModel = new ObjModel(
            source,
            DEFAULT_OBJECT_COLOR,
            DEFAULT_LIGHT_SOURCE_POSITION,
            DEFAULT_AMBIENT_LIGHT_INTENSITY
        );
        Screen screen = new Screen(
            DEFAULT_SCREEN_CENTER,
            DEFAULT_SCREEN_WIDTH_AXIS,
            DEFAULT_SCREEN_HEIGHT_AXIS,
            DEFAULT_SCREEN_NORMAL_AXIS,
            DEFAULT_SCREEN_PIXEL_WIDTH,
            DEFAULT_SCREEN_PIXEL_HEIGHT
        );

        objModel.setMin(DEFAULT_MODEL_MIN_POSITION, DEFAULT_SCREEN_NORMAL_AXIS);

        return renderImageFromObjModel(
            objModel,
            screen,
            DEFAULT_CAMERA_POSITION
        );
    }
}
