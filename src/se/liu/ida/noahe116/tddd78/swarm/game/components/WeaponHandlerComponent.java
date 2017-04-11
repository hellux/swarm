package se.liu.ida.noahe116.tddd78.swarm.game.components;

import se.liu.ida.noahe116.tddd78.swarm.game.weapons.Weapon;

public abstract class WeaponHandlerComponent {
    private Weapon[] equipped;
    private List<Weapon>[] weapons;

    public Weapon getEquipped(int slot) {
        return this.equipped[slot];
    }

    public void fire(int slot) {
        this.equipped[slot].fire();
    }

    public void add(Weapon w, int slot) {
        this.weapons[slot].add(w);
    }

    public void clear() {
        for (List<Weapon> list : weapons) {
            list.clear();
        }
    }
}
