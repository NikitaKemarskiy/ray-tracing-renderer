package org.nikita.renderer;

import com.paulok777.formats.Image;
import com.paulok777.formats.Pixel;
import org.nikita.geometry.Axis;
import org.nikita.geometry.Color;
import org.nikita.geometry.Vector;
import org.nikita.geometry.Ray;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class RayTracingObjRenderer implements Renderer {

    private double modelMinPosition;
    private Vector camera;
    private ObjModel objModel;
    private Screen screen;

    public RayTracingObjRenderer(double modelMinPosition, Vector camera, ObjModel objModel, Screen screen) {
        this.modelMinPosition = modelMinPosition;
        this.camera = camera;
        this.objModel = objModel;
        this.screen = screen;
    }

    @Override
    public Image render(InputStream inputStream) throws IOException {
        objModel.populateTriangles(inputStream);
        objModel.setMin(modelMinPosition, screen.getNormalAxis());
        objModel.init();

        return renderImageFromObjModel(objModel, screen, camera);
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

                double colorIntensity = objModel.getColorIntensity(ray);

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
