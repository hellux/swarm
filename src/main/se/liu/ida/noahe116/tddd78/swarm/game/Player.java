package se.liu.ida.noahe116.tddd78.swarm.game;

import se.liu.ida.noahe116.tddd78.swarm.common.Vector2D;

public class Player extends MovingEntity {
    private static double DRAG = 0.9;
    private static double MAX_THRUST = 0.5;
    private static double MAX_SPEED = 1;

    private double thrustPower = 0;
    private boolean thrust = false;

    @Override
    public void tick() { 
        if (this.thrust) {
            Vector2D acceleration =
                Vector2D.fromLengthRotation(this.thrustPower*MAX_THRUST, this.rotation);
            this.accelerate(acceleration);
        }

        this.velocity = this.velocity.multiply(DRAG);
        this.position = this.position.add(this.velocity);
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public void setThrust(boolean t) {
        this.thrust = t;
    }

    public void setThrustPower(double tp) {
        this.thrustPower = tp;
    }
}
