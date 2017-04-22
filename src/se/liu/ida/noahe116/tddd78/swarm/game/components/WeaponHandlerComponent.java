package se.liu.ida.noahe116.tddd78.swarm.game.components;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.logging.*;

import se.liu.ida.noahe116.tddd78.swarm.game.*;
import se.liu.ida.noahe116.tddd78.swarm.game.weapons.*;

public class WeaponHandlerComponent extends LiveComponent {
    private static final Logger LOGGER =
        Logger.getLogger(WeaponHandlerComponent.class.getName());
        
    private static final int DEFAULT_SLOT_COUNT = 1;

    public WeaponSlot[] slots;

    /**
     * List of EquippedWeapon at a entity's slot of weapons.
     **/
    public class WeaponSlot {
        private EquippedWeapon equipped;
        private boolean fire;
        private int cooldown;
        private AbstractMap<WeaponType, EquippedWeapon> weapons = new HashMap<>();

        public boolean has(WeaponType type) {
            return this.weapons.containsKey(type);
        }

        public void add(WeaponType type) {
            EquippedWeapon ew = new EquippedWeapon(type);
            this.weapons.put(type, ew); 
            if (this.equipped == null) {
                this.equipped = ew;
            }
        }

        public void equip(WeaponType type) {
            this.equipped = weapons.get(type);
        }

        public void addAmmo(WeaponType type, int ammo) {
            this.weapons.get(type).addAmmo(ammo);
        }

        public void setFire(boolean state) {
            this.fire = state;
        }

        public WeaponType getEquippedType() {
            return this.equipped.type;
        }

        public void update(Entity e) {
            if (this.cooldown > 0) {
                this.cooldown -= 1;
            } else if (this.fire) {
                this.cooldown = this.equipped.fire(e);
            }
        }
    }

    /**
     * Wrapper for Weapon that handles ammo.
     **/
    public class EquippedWeapon {
        public final WeaponType type;
        private int ammo = -1;
        private Weapon weapon;

        public EquippedWeapon(WeaponType type) {
            this.type = type;
            this.weapon = WeaponCreator.get(type);
        }
        
        public void addAmmo(int ammo) {
            this.ammo += ammo;
        }

        public int fire(Entity e) {
            if (ammo > 0) {
                this.ammo -= 1;
            }
            if (ammo != 0) {
                this.weapon.fire(e);
            }
            return this.weapon.getCooldown();
        }
    }

    public WeaponHandlerComponent(int slotCount) {
        this.slots = new WeaponSlot[slotCount];
        for (int i = 0; i < slotCount; i++) {
            this.slots[i] = new WeaponSlot();
        }
    }

    public WeaponHandlerComponent() {
        this(DEFAULT_SLOT_COUNT);
    }

    private WeaponSlot slotWithWeapon(WeaponType type) {
        for (WeaponSlot slot : this.slots) {
            if (slot.has(type)) {
                return slot;
            }
        }

        return null;
    }

    private WeaponSlot getSlot(int slot) {
        if (0 <= slot && slot <= this.slots.length) {
            return this.slots[slot];
        } else {
            throw new IllegalArgumentException("invalid slot: " + slot);
        }
    }

    public WeaponType getEquippedType(int slot) {
        return this.getSlot(slot).getEquippedType();
    }

    public void fire(boolean state, int slot) {
        this.getSlot(slot).fire = state;
    }

    public void equip(WeaponType type) {
        WeaponSlot slot = slotWithWeapon(type);
        if (slot == null) {
            LOGGER.log(Level.WARNING, "entity has no weapon of type: " + type);
        } else {
            slot.equip(type);
        }
    }

    public void add(WeaponType type, int slot) {
        if (this.slotWithWeapon(type) == null) {
            this.getSlot(slot).add(type);
        } else {
            LOGGER.log(Level.WARNING, "weapon is already equipped: " + type);
        }
    }

    public void addAmmo(WeaponType type, int ammo) {
        WeaponSlot slot = this.slotWithWeapon(type);
        if (slot != null) {
            slot.addAmmo(type, ammo);
        }
    }

    public void update() {
        for (WeaponSlot slot : this.slots) {
            slot.update(this.entity);
        }
    }
}
