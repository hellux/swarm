package se.liu.ida.noahe116.tddd78.swarm.game.weapons;

import se.liu.ida.noahe116.tddd78.swarm.game.*;

public abstract class Weapon {
    private int ammunition = 0;
    private final int cooldown;
        
    protected Weapon(int cooldown) {
        this.cooldown = cooldown;
    }

    protected Weapon() {
        this(0);
    }

    public void addAmmunition(int ammo) {
        this.ammunition += ammo;
    }

    public int getAmmunition() {
        return this.ammunition;
    }

    public int getCooldown() {
        return this.cooldown;
    }

    public abstract void fire(Entity e);
}
