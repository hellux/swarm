package se.liu.ida.noahe116.tddd78.swarm.game.components;

import se.liu.ida.noahe116.tddd78.swarm.game.weapons.*;

/**
 * Wrapper for easy access to controls and info for a player entity.
 **/
public class PlayerComponent extends Component {
    private static final WeaponType[] PRIMARY_WEAPONS =
        new WeaponType[] {
            WeaponType.DEFAULT,
            WeaponType.RED_LASER,
            WeaponType.SPREAD,
            WeaponType.QUAD,
    };

    public void setThrust(boolean state) {
        this.entity.get(ThrustComponent.class).setThrust(state);
    }

    public boolean hasThrust() {
        return this.entity.get(ThrustComponent.class).hasThrust();
    }

    public double shieldStrength() {
        HealthComponent hc = this.entity.get(HealthComponent.class);
        return (double) hc.getShieldStrength() / hc.getMaxShieldStrength();
    }
    
    public int extraLives() {
        return this.entity.get(HealthComponent.class).getExtraLives();
    }

    public void setThrustPower(double tp) {
        this.entity.get(ThrustComponent.class).setThrustPower(tp);
    }

    public void setRotation(double rotation) {
        this.entity.get(PositionComponent.class).setRotation(rotation);
    }

    public void equipPrimary(int slot) {
        this.entity.get(WeaponHandlerComponent.class)
            .equip(PRIMARY_WEAPONS[slot-1]);
    }

    public void firePrimary(boolean state) {
        this.entity.get(WeaponHandlerComponent.class).fire(state, 0);
    }
}
