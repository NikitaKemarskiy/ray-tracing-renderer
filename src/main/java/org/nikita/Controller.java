package org.nikita;

import org.nikita.renderer.RayTracingRenderer;
import org.nikita.renderer.Renderer;

import java.io.IOException;

public class Controller {
    public static void main(String[] args) {
        String source = "models/cow/cow.obj";
        Renderer renderer = new RayTracingRenderer();

        try {
            renderer.render(source);
        } catch (IOException err) {
            System.err.println(err);
        }
    }
}
