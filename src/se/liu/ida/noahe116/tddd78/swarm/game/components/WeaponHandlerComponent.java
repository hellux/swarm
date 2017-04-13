package se.liu.ida.noahe116.tddd78.swarm.game.components;

import java.util.List;
import java.util.ArrayList;

import se.liu.ida.noahe116.tddd78.swarm.game.weapons.Weapon;

public class WeaponHandlerComponent extends Component {
    private static final int DEFAULT_SLOT_COUNT = 1;
    private int[] equipped;
    private List<List<Weapon>> weapons;

    public WeaponHandlerComponent(int slotCount) {
        this.equipped = new int[slotCount];

        this.weapons = new ArrayList<List<Weapon>>(slotCount);
        while (slotCount-- > 0) {
            this.weapons.add(new ArrayList<Weapon>());
        }
    }

    public WeaponHandlerComponent() {
        this(DEFAULT_SLOT_COUNT);
    }

    public Weapon getEquipped(int slot) {
        return this.weapons.get(slot).get(this.equipped[slot]);
    }

    public void fire(int slot) {
        this.getEquipped(slot).fire();
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
}
