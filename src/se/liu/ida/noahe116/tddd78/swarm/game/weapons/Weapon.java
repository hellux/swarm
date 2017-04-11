package se.liu.ida.noahe116.tddd78.swarm.game.weapons;

public abstract class Weapon {
    private int ammunition = 0;

    public abstract void fire();

    public void addAmmunition(int ammo) {
        this.ammunition += ammo;
    }

    public int getAmmunition() {
        return this.ammunition;
    }
}
