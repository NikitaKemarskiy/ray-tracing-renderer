package org.nikita.renderer;

import de.javagl.obj.*;
import org.nikita.geometry.Polygon;
import org.nikita.geometry.Vector;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ObjModel {
    Set<Polygon> polygons;

    public ObjModel(String source) throws IOException {
        polygons = new HashSet<>();

        try (InputStream inputStream = new FileInputStream(source)) {
            Obj obj = ObjUtils.convertToRenderable(ObjReader.read(inputStream));
            for (int faceIndex = 0; faceIndex < obj.getNumFaces(); faceIndex++) {
                ObjFace face = obj.getFace(faceIndex);
                Polygon polygon = new Polygon();
                for (int vertexNumber = 0; vertexNumber < face.getNumVertices(); vertexNumber++) {
                    FloatTuple floatTuple = obj.getVertex(face.getVertexIndex(vertexNumber));
                    polygon.addVertex(new Vector(floatTuple.getX(), floatTuple.getY(), floatTuple.getZ()));
                }
                polygons.add(polygon);
            }
        }
    }

    @Override
    public String toString() {
        return String.format(
            "Polygons: [%s]",
            polygons.stream()
                .map(polygon -> String.format("{ %s }", polygon.toString()))
                .collect(Collectors.joining(", "))
        );
    }
}
