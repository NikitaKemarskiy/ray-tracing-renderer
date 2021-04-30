package org.nikita.renderer;

import com.paulok777.formats.Image;

import java.io.IOException;
import java.io.InputStream;

public interface Renderer {

    Image render(InputStream inputStream) throws IOException;
}
