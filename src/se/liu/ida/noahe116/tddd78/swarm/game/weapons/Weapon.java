package se.liu.ida.noahe116.tddd78.swarm.game.weapons;

import se.liu.ida.noahe116.tddd78.swarm.game.entities.Entity;
import se.liu.ida.noahe116.tddd78.swarm.game.level.*;

/**
 * Abstract class for weapons that specify properties and behaviour.
 */
public abstract class Weapon {
    private final int cooldown;
    private final WeaponType type;
        
    protected Weapon(int cooldown, WeaponType type) {
        this.cooldown = cooldown;
        this.type = type;
    }

    public int getCooldown() {
        return this.cooldown;
    }
    
    public WeaponType getType() {
        return this.type;
    }

    public abstract void fire(Entity e, GameLevel level);
}
