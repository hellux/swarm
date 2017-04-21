package se.liu.ida.noahe116.tddd78.swarm.game.components;

import se.liu.ida.noahe116.tddd78.swarm.game.Entity;

/**
 * Wrapper for easy access to controls and info for a player entity.
 **/
public class PlayerComponent extends Component {
    public void setThrust(boolean state) {
        this.entity.get(ThrustComponent.class).setThrust(state);
    }

    public boolean hasThrust() {
        return this.entity.get(ThrustComponent.class).hasThrust();
    }

    public void setThrustPower(double tp) {
        this.entity.get(ThrustComponent.class).setThrustPower(tp);
    }

    public void setRotation(double rotation) {
        this.entity.get(PositionComponent.class).setRotation(rotation);
    }

    public void firePrimary(boolean state) {
        this.entity.get(WeaponHandlerComponent.class).fire(state, 0);
    }

    public void fireSecondary(boolean state) {
        this.entity.get(WeaponHandlerComponent.class).fire(state, 1);
    }
}
