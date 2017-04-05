package se.liu.ida.noahe116.tddd78.swarm.game.components;

import se.liu.ida.noahe116.tddd78.swarm.common.Vector2D;
import se.liu.ida.noahe116.tddd78.swarm.game.Entity;

public class PlayerComponent extends LiveComponent {
    private static final double DRAG = 0.03;
    private static final double MAX_THRUST = 10;

    private double thrustPower;
    private boolean thrust;
    private WeaponComponent equippedWeapon;

    public PlayerComponent(Entity entity) {
        entity.get(PositionComponent.class).setDrag(DRAG);
    }

    public void update() { 
        if (this.thrust) {
            Vector2D acceleration =
                Vector2D.fromLengthRotation(this.thrustPower*MAX_THRUST,
                    this.entity.get(PositionComponent.class).getRotation());
            this.entity.get(PositionComponent.class).accelerate(acceleration);
        }
    }

    public void setThrust(boolean t) {
        this.thrust = t;
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

    public WeaponComponent getEquippedWeapon() {
        return this.equippedWeapon;
    }
}
