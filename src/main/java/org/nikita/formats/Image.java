package org.nikita.formats;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Image {
    private int height;
    private int width;
    private List<Pixel> pixels;

    public Image(int width, int height) {
        this.width = width;
        this.height = height;
        pixels = new LinkedList<>();
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void addPixel(Pixel pixel) {
        this.pixels.add(pixel);
    }

    public List<Pixel> getPixels() {
        return pixels;
    }

    @Override
    public String toString() {
        return String.format("Image { width: %d; height: %d }", width, height);
    }
}
