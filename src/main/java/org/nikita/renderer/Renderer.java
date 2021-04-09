package org.nikita.renderer;

import org.nikita.formats.Image;

import java.io.IOException;

public interface Renderer {
    Image render(String source) throws IOException;
}
