package se.liu.ida.noahe116.tddd78.swarm.game.components;

import se.liu.ida.noahe116.tddd78.swarm.common.Vector2D;

public class PositionComponent extends LiveComponent {
    private final Vector2D position = new Vector2D();
    private final Vector2D velocity = new Vector2D();
    private final Vector2D acceleration = new Vector2D();

    private double rotation = 0;

    private double drag;

    public PositionComponent(double x, double y) {
        this.position.x = x;
        this.position.y = y;
    }

    public PositionComponent() {
        this(0, 0);
    }

    public void update() {
        this.position.add(this.velocity);
        this.velocity.add(this.acceleration);
        this.velocity.multiply(1-this.drag);
    }

    public Vector2D futurePos(double interpolation) {
        if (this.active) {
            Vector2D futureVel = Vector2D.multiply(
                Vector2D.add(this.velocity, this.acceleration),
                (1-this.drag)*interpolation
            );
            return Vector2D.add(this.position,
                                Vector2D.multiply(futureVel, interpolation));
        } else {
            return this.position;
        }
    }

    public void move(Vector2D movement) {
        this.position.add(movement);
    }

    public void accelerate(Vector2D acc) {
        this.velocity.add(acc);
    }

    public void setAcceleration(double x, double y) {
        this.acceleration.x = x;
        this.acceleration.y = y;
    }

    public void setAcceleration(Vector2D acc) {
        this.setAcceleration(acc.x, acc.y);
    }

    public void setRotation(double rotation) {
        this.rotation = rotation; 
    }

    public void setDrag(double drag) {
        this.drag = drag;
    }

    public double getRotation() {
        return this.rotation;
    }

    public Vector2D getPosition() {
        return this.position;
    }

    public Vector2D getVelocity() {
        return this.velocity;
    }
}
