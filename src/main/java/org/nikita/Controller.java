package org.nikita;

import org.nikita.formats.Image;
import org.nikita.renderer.RayTracingObjRenderer;
import org.nikita.renderer.Renderer;

import java.io.IOException;

public class Controller {
    public static void main(String[] args) {
        String source = "models/cube/cube.obj";
        Renderer renderer = new RayTracingObjRenderer();

        try {
            Image image = renderer.render(source);
            System.out.println(image.getPixels().size());
        } catch (IOException err) {
            System.err.println(err);
        }
    }
}
