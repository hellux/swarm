package se.liu.ida.noahe116.tddd78.swarm.game.components;

import java.util.List;

import se.liu.ida.noahe116.tddd78.swarm.common.Vector2D;
import se.liu.ida.noahe116.tddd78.swarm.game.Entity;

public class PlayerComponent extends LiveComponent {
    private static final double DRAG = 0.05;
    private static final double MAX_THRUST = 12;

    private double thrustPower;
    private boolean thrust;

    public PlayerComponent(Entity entity) {
        entity.get(PositionComponent.class).setDrag(DRAG);
    }

    public void update() { 
        if (this.thrust) {
            this.entity.get(PositionComponent.class).setAcceleration(
                Vector2D.fromLengthRotation(
                    this.thrustPower*MAX_THRUST,
                    this.entity.get(PositionComponent.class).getRotation()
                )
            );
        } else {
            this.entity.get(PositionComponent.class).setAcceleration(0, 0);
        }
    }

    public void setThrust(boolean state) {
        this.thrust = state;
    }

    public boolean isThrust() {
        return this.thrust;
    }

    public void setThrustPower(double tp) {
        this.thrustPower = tp;
    }

    public void setRotation(double rotation) {
        this.entity.get(PositionComponent.class).setRotation(rotation);
    }

    public void firePrimary(boolean state) {
        this.entity.get(WeaponHandlerComponent.class).fire(0);
    }

    public void fireSecondary(boolean state) {
        this.entity.get(WeaponHandlerComponent.class).fire(1);
    }
}
