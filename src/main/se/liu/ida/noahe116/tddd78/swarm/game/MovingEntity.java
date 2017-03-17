package se.liu.ida.noahe116.tddd78.swarm.game;

import se.liu.ida.noahe116.tddd78.swarm.common.Vector2D;

public abstract class MovingEntity extends Entity {
    protected Vector2D velocity = new Vector2D(); 

    public void tick() {
        this.position = this.position.add(this.velocity);
    }

    protected void accelerate(Vector2D acceleration) {
        this.velocity = this.velocity.add(acceleration);
    }

    public Vector2D getVelocity() {
        return this.velocity.copy();
    }
}
