package org.nikita.geometry;

public class Color {
    private int red;
    private int green;
    private int blue;

    public final static Color WHITE = new Color(255, 255, 255);
    public final static Color BLACK = new Color(0, 0, 0);

    public Color(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public Color multiply(double value) {
        return new Color(
            (int) (red * value),
            (int) (green * value),
            (int) (blue * value)
        );
    }

    public int getRed() {
        return this.red;
    }

    public int getGreen() {
        return this.green;
    }

    public int getBlue() {
        return this.blue;
    }

    @Override
    public String toString() {
        return "Color{" +
                "red=" + red +
                ", green=" + green +
                ", blue=" + blue +
                '}';
    }
}
