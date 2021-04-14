package org.nikita.geometry;

public class Color {
    private byte red;
    private byte green;
    private byte blue;

    public final static Color WHITE = new Color((byte) 255, (byte) 255, (byte) 255);
    public final static Color BLACK = new Color((byte) 0, (byte) 0, (byte) 0);

    public Color(byte red, byte green, byte blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public Color multiply(double value) {
        return new Color(
            (byte) (red * value),
            (byte) (green * value),
            (byte) (blue * value)
        );
    }

    public byte getRed() {
        return this.red;
    }

    public byte getGreen() {
        return this.green;
    }

    public byte getBlue() {
        return this.blue;
    }

    public String toString() {
        return "Pixel{red=" + this.red + ", green=" + this.green + ", blue=" + this.blue + '}';
    }
}
