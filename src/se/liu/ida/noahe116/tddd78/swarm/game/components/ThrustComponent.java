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

    /**
     * Set the thrust power.
     * if the ratio is larger than 1 or lower than 0
     * the thrust will still be limited within 0 and 1.
     * (exception if outside range is not used 
     * in order to avoid having to account for floating 
     * point errors)
     * @param tp ratio of max thrust 
     **/
    public void setThrustPower(double tp) {
        if (tp < 0) {
            this.thrustPower = 0;
        } else if (tp > 1) {
            this.thrustPower= 1;
        } else {
            this.thrustPower = tp;
        }
    }
}
