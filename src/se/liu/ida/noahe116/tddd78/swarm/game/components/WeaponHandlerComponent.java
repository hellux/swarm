package se.liu.ida.noahe116.tddd78.swarm.game.components;

import java.util.List;
import java.util.ArrayList;

import se.liu.ida.noahe116.tddd78.swarm.game.weapons.Weapon;

public class WeaponHandlerComponent extends LiveComponent {
    private static final int DEFAULT_SLOT_COUNT = 1;

    private final int slotCount;
    private int[] equipped;
    private int[] cooldown;
    private boolean[] fire;
    private List<List<Weapon>> weapons;

    public WeaponHandlerComponent(int slotCount) {
        this.slotCount = slotCount;
        this.equipped = new int[slotCount];
        this.cooldown = new int[slotCount];
        this.fire = new boolean[slotCount];

        this.weapons = new ArrayList<>(slotCount);
        while (slotCount-- > 0) {
            this.weapons.add(new ArrayList<>());
        }
    }

    public WeaponHandlerComponent() {
        this(DEFAULT_SLOT_COUNT);
    }

    public Weapon getEquipped(int slot) {
        return this.weapons.get(slot).get(this.equipped[slot]);
    }

    public void fire(boolean state, int slot) {
        this.fire[slot] = state;
    }

    public void add(Weapon w) {
        this.add(w, 0);
    }

    public void add(Weapon w, int slot) {
        this.weapons.get(slot).add(w);
    }

    public void clear() {
        for (List<Weapon> list : weapons) {
            list.clear();
        }
    }

    public void update() {
        for (int s = 0; s < this.slotCount; s++) {
            if (this.cooldown[s] > 0) {
                this.cooldown[s] -= 1;
            } else if (this.fire[s]) {
                this.getEquipped(s).fire(this.entity);
                this.cooldown[s] = getEquipped(s).getCooldown();
            }
        }
    }
}
