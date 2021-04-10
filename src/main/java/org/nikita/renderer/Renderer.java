package org.nikita.renderer;

import com.paulok777.formats.Image;

import java.io.IOException;

public interface Renderer {
    Image render(String source) throws IOException;
}
