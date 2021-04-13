package org.nikita;

import com.paulok777.formats.Image;
import com.paulok777.writers.BmpImageWriter;
import com.paulok777.writers.PpmImageWriter;
import org.nikita.renderer.RayTracingObjRenderer;
import org.nikita.renderer.Renderer;

import java.io.File;
import java.io.IOException;

public class Controller {
    public static void main(String[] args) {
        String source = "models/cube/cube.obj";
        Renderer renderer = new RayTracingObjRenderer();

        try {
            Image image = renderer.render(source);
            PpmImageWriter ppmImageWriter = new PpmImageWriter(new File("renders/cube.ppm"));
            ppmImageWriter.write(image);
            BmpImageWriter bmpImageWriter = new BmpImageWriter(new File("renders/cube.bmp"));
            bmpImageWriter.write(image);

            System.out.println(image);
            System.out.println(image.getPixels().length);
        } catch (IOException err) {
            System.err.println(err);
        }
    }
}
