package se.liu.ida.noahe116.tddd78.swarm.game;

import se.liu.ida.noahe116.tddd78.swarm.common.Vector2D;

public abstract class Entity {
    protected Vector2D position = new Vector2D();
    protected double rotation = 0;
    
    public double getRotation() {
        return this.rotation;
    }

    public Vector2D getPosition() {
        return this.position.copy();
    }
}
