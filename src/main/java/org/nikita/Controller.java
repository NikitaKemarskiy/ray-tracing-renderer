package org.nikita;

import com.paulok777.Arguments;
import com.paulok777.extensions.ImageExtension;
import com.paulok777.formats.Image;
import com.paulok777.writers.BmpImageWriter;
import com.paulok777.writers.ImageWriter;
import com.paulok777.writers.PpmImageWriter;
import org.nikita.renderer.Renderer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class Controller {

    private static final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    private Renderer renderer;

    public Controller(Renderer renderer) {
        this.renderer = renderer;
    }

    public void run(String[] args) {
        try {
            if (args.length != 2) {
                throw new IllegalArgumentException("You should pass 2 parameters");
            }

            Map<String, String> mapArgs = parse(args);
            String source = mapArgs.get(Arguments.SOURCE);
            String output = mapArgs.get(Arguments.OUTPUT);

            render(source, output);
        } catch (Exception err) {
            System.err.println(err.getMessage());
        }
    }

    private void render(String source, String output) throws IOException {
        System.out.println(">>> Starting to render at: " + getCurrentDateTime());

        Image image;
        try (InputStream inputStream = new FileInputStream(source)) {
            image = renderer.render(inputStream);
        }

        System.out.println(">>> Render finished at: " + getCurrentDateTime());

        ImageWriter writer = getWriter(output);

        writer.write(image);
    }

    private String getCurrentDateTime() {
        return dateTimeFormat.format(LocalDateTime.now());
    }

    private static Map<String, String> parse(String[] args) {
        Map<String, String> mapArgs = new HashMap<>();

        for (String arg : args)  {
            String key = arg.substring(0, arg.indexOf('=')).toLowerCase();
            String value = arg.substring(arg.indexOf('=') + 1).toLowerCase();
            mapArgs.put(key, value);
        }

        return mapArgs;
    }

    private ImageWriter getWriter(String output) {
        String[] dividedByDot = output.split("\\.");
        ImageExtension extension = ImageExtension.valueOf(dividedByDot[dividedByDot.length - 1].toUpperCase());

        return switch (extension) {
            case BMP -> new BmpImageWriter(new File(output));
            case PPM -> new PpmImageWriter(new File(output));
        };
    }
}
