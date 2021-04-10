package org.nikita.geometry;

public class Ray {
    private Vector start;
    private Vector direction;

    public Ray(Vector start, Vector direction) {
        this.start = start;
        this.direction = direction;
    }

    public Vector getStart() {
        return start;
    }

    public void setStart(Vector start) {
        this.start = start;
    }

    public Vector getDirection() {
        return direction;
    }

    public void setDirection(Vector direction) {
        this.direction = direction;
    }
}
