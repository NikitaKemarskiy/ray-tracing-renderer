package org.nikita.renderer;

import org.nikita.geometry.Vector;

public class Camera {
    private Vector position;
    private Vector direction;

    public Camera(Vector position, Vector direction) {
        this.position = position;
        this.direction = direction;
    }

    public Vector getPosition() {
        return position;
    }

    public void setPosition(Vector position) {
        this.position = position;
    }

    public Vector getDirection() {
        return direction;
    }

    public void setDirection(Vector direction) {
        this.direction = direction;
    }
}
