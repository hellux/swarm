package se.liu.ida.noahe116.tddd78.swarm.game.components;

import se.liu.ida.noahe116.tddd78.swarm.common.Vector2D;
import se.liu.ida.noahe116.tddd78.swarm.game.level.*;

/**
 * Handles thrust mechanics for entities.
 **/
public class ThrustComponent extends LiveComponent {
    private static final double MAX_THRUST = 4;

    private double thrustPower;
    private boolean thrust;

    @Override
    public void update(GameLevel level) { 
        if (this.thrust) {
            this.entity.get(PositionComponent.class).setAcceleration(
                Vector2D.fromLengthRotation(
                    this.thrustPower*MAX_THRUST,
                    this.entity.get(PositionComponent.class).getRotation()
                )
            );
        } else {
            this.entity.get(PositionComponent.class).setAcceleration(new Vector2D());
        }
    }

    public void setThrust(boolean state) {
        this.thrust = state;
    }

    public boolean hasThrust() {
        return this.thrust;
    }

    public void setThrustPower(double tp) {
        this.thrustPower = tp;
    }
}
