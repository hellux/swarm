package se.liu.ida.noahe116.tddd78.swarm.game.weapons;

import se.liu.ida.noahe116.tddd78.swarm.game.entities.Entity;

public abstract class Weapon {
    private final int cooldown;
    private final WeaponType type;
        
    protected Weapon(int cooldown, WeaponType type) {
        this.cooldown = cooldown;
        this.type = type;
    }

    protected Weapon(WeaponType type) {
        this(0, type);
    }

    public int getCooldown() {
        return this.cooldown;
    }
    
    public WeaponType getType() {
        return this.type;
    }

    public abstract void fire(Entity e);
}
