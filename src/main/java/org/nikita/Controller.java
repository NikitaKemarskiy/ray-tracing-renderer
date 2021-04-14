package org.nikita;

import com.paulok777.formats.Image;
import com.paulok777.writers.PpmImageWriter;
import org.nikita.renderer.RayTracingObjRenderer;
import org.nikita.renderer.Renderer;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Controller {
    public static void main(String[] args) {
        String source = "models/cow/cow.obj";
        Renderer renderer = new RayTracingObjRenderer();

        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

            System.out.println(">>> Starting to render at: " + dtf.format(LocalDateTime.now()));

            Image image = renderer.render(source);

            System.out.println(">>> Render finished at: " + dtf.format(LocalDateTime.now()));

            PpmImageWriter ppmImageWriter = new PpmImageWriter(new File("renders/cow.ppm"));
            ppmImageWriter.write(image);
        } catch (IOException err) {
            System.err.println(err);
        }
    }
}
