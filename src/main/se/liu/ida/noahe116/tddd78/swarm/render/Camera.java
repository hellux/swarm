package se.liu.ida.noahe116.tddd78.swarm.render;

import se.liu.ida.noahe116.tddd78.swarm.common.Vector2D;

public class Camera {
    private Vector2D position = new Vector2D();
    private Vector2D size = new Vector2D();

    private void setPosition(Vector2D position) {
        this.position = position;
    }

    private void setSize(Vector2D size) {
        this.size = size;
    }

    private Vector2D getPosititon() {
        return this.position;
    }
    
    private Vector2D getSize() {
        return this.size;
    }
}
