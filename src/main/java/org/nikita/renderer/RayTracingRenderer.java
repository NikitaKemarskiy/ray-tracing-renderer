package org.nikita.renderer;

import org.nikita.formats.Image;

import java.io.IOException;

public class RayTracingRenderer implements Renderer {
    @Override
    public Image render(String source) throws IOException {
        ObjModel objModel = new ObjModel(source);

        System.out.println(objModel);

        return null;
    }
}
