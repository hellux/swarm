package se.liu.ida.noahe116.tddd78.swarm.game.components;

import se.liu.ida.noahe116.tddd78.swarm.common.*;
import se.liu.ida.noahe116.tddd78.swarm.game.level.*;

/**
 * Handle positioning (incl speed, accel) of entities.
 **/
public class PositionComponent extends LiveComponent {
    private static final double DRAG = 0.94;

    private final Vector2D position = new Vector2D();
    private final Vector2D velocity = new Vector2D();
    private final Vector2D acceleration = new Vector2D();

    private double rotation = 0;
    private double rotationalSpeed = 0;
    private double drag = 1;

    public PositionComponent(Vector2D pos) {
        this.position.x = pos.x;
        this.position.y = pos.y;
    }

    public PositionComponent() {
        this(new Vector2D());
    }

    @Override
    public void update(GameLevel level) {
        this.position.add(this.velocity);
        this.position.x = Math2.floorMod(this.position.x, level.getSize());
        this.position.y = Math2.floorMod(this.position.y, level.getSize());

        this.rotation = Math2.floorMod(this.rotation + this.rotationalSpeed, 2*Math.PI);

        this.velocity.add(this.acceleration);
        this.velocity.multiply(this.drag);
    }

    public Vector2D futurePos(double interpolation) {
        if (this.active && !this.entity.isKilled()) {
            Vector2D futureVel = Vector2D.multiply(
                Vector2D.add(
                    this.velocity, 
                    Vector2D.multiply(
                        this.acceleration,
                        interpolation
                    )
                ),
                interpolation*(1-(1-drag)*interpolation)
            );
            return Vector2D.add(this.position,
                                futureVel);
        } else {
            return this.position;
        }
    }

    public double futureRot(double interpolation) {
        if (this.active) {
            return this.rotation + this.rotationalSpeed * interpolation;
        } else {
            return this.rotation;
        }
    }

    public void stop() {
        this.velocity.x = 0;
        this.velocity.y = 0;
        this.acceleration.x = 0;
        this.acceleration.y = 0;
    }

    public void accelerate(Vector2D acc) {
        this.velocity.add(acc);
    }

    public void setPosition(Vector2D pos) {
        this.position.set(pos);
    }

    public void setVelocity(Vector2D vel) {
        this.velocity.set(vel);
    }

    public void setAcceleration(Vector2D acc) {
        this.acceleration.set(acc);
    }

    public void setRotation(double rotation) {
        this.rotation = rotation; 
    }

    public void setRotationalSpeed(double speed) {
        this.rotationalSpeed = speed;
    }

    public void setDrag(boolean b) {
        this.drag = b ? DRAG : 1;
    }

    public Vector2D getPosition() {
        return this.position.copy();
    }

    public Vector2D getVelocity() {
        return this.velocity.copy();
    }

    public double getRotation() {
        return this.rotation;
    }

}
