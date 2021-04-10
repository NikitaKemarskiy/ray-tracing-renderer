package org.nikita.formats;

public class Pixel {
    private byte red;
    private byte green;
    private byte blue;

    public final static Pixel BLACK = new Pixel((byte) 0, (byte) 0, (byte) 0);
    public final static Pixel WHITE = new Pixel((byte) 255, (byte) 255, (byte) 255);

    public Pixel(byte red, byte green, byte blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public byte getRed() {
        return red;
    }

    public byte getGreen() {
        return green;
    }

    public byte getBlue() {
        return blue;
    }

    @Override
    public String toString() {
        return "Pixel{" +
                "red=" + red +
                ", green=" + green +
                ", blue=" + blue +
                '}';
    }
}
