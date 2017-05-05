package se.liu.ida.noahe116.tddd78.swarm.game.components;

import java.util.AbstractMap;
import java.util.EnumMap;
import java.util.logging.*;

import se.liu.ida.noahe116.tddd78.swarm.game.entities.Entity;
import se.liu.ida.noahe116.tddd78.swarm.game.weapons.*;
import se.liu.ida.noahe116.tddd78.swarm.game.level.*;

/**
 * Handle equipped weapons of an entity.
 *
 * An entity can has any amount of weapon slots (decided on instantiation of handler).
 * Each weapon slot can have any amount of weapons (can be added at any time).
 **/
public class WeaponHandlerComponent extends LiveComponent {
    private static final Logger LOGGER =
        Logger.getLogger(WeaponHandlerComponent.class.getName());
        
    private WeaponSlot[] slots;

    /**
     * List of EquippedWeapon at a entity's slot of weapons.
     **/
    public class WeaponSlot {
        private EquippedWeapon equipped = null;
        private boolean fire;
        private int cooldown;
        private AbstractMap<WeaponType, EquippedWeapon> weapons = new EnumMap<>(WeaponType.class);

        public boolean has(WeaponType type) {
            return this.weapons.containsKey(type);
        }

        public boolean hasAmmo(WeaponType type) {
            return this.weapons.get(type).hasAmmo();
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

        public void setUnlimitedAmmo(WeaponType type, boolean state) {
            this.weapons.get(type).setUnlimitedAmmo(state);
        }
        
        public WeaponType getEquippedType() {
            return this.equipped.type;
        }

        public void update(Entity e, GameLevel level) {
            if (this.cooldown > 0) {
                this.cooldown -= 1;
            } else if (this.fire) {
                this.cooldown = this.equipped.fire(e, level);
            }
        }
    }

    /**
     * Wrapper for Weapon that also handles ammo.
     **/
    public class EquippedWeapon {
        /**
         * weapon type of equipped weapon
         */
        public final WeaponType type;

        private int ammo = 0;
        private Weapon weapon;
        private boolean unlimitedAmmo = false;

        public EquippedWeapon(WeaponType type) {
            this.type = type;
            this.weapon = WeaponCreator.get(type);
        }

        public void setUnlimitedAmmo(boolean state) {
            this.unlimitedAmmo = state;
        }
        
        public void addAmmo(int ammo) {
            this.ammo += ammo;
        }

        public boolean hasAmmo() {
            return this.unlimitedAmmo || this.ammo > 0;
        }


        public int fire(Entity e, GameLevel level) {
            if (this.unlimitedAmmo) {
                this.weapon.fire(e, level);
            } else if (this.ammo > 0) {
                this.weapon.fire(e, level);
                this.ammo -= 1;
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

    /**
     * Get the weapon type of the currently equipped weapon in a slot.
     * @param slot a slot
     * @return weapon type of equipped weapon.
     **/
    public WeaponType getEquippedType(int slot) {
        return this.getSlot(slot).getEquippedType();
    }

    public void fire(boolean state, int slot) {
        this.getSlot(slot).fire = state;
    }

    public void equip(WeaponType type) {
        WeaponSlot slot = slotWithWeapon(type);
        if (slot == null) {
            throw new IllegalArgumentException("entity has no weapon of type: " + type);
        } else if (slot.hasAmmo(type)) {
            slot.equip(type);
        }
    }

    /**
     * Add a weapon of a specific type to a weapon slot.
     * @param type type of weapon
     * @param slot weapon slot
     */
    public void add(WeaponType type, int slot) {
        if (this.slotWithWeapon(type) == null) {
            this.getSlot(slot).add(type);
        }
    }

    public void addUnlimited(WeaponType type, int slot) {
        this.add(type, slot);
        // cannot return null because the weapon type was just added
	this.slotWithWeapon(type).setUnlimitedAmmo(type, true);
    }

    public void addAmmo(WeaponType type, int ammo) {
        WeaponSlot slot = this.slotWithWeapon(type);
        if (slot != null) {
            slot.addAmmo(type, ammo);
        }
    }

    /**
     * Update ammo and cooldown of equipped weapons.
     * @param level game level
     **/
    @Override
    public void update(GameLevel level) {
        for (WeaponSlot slot : this.slots) {
            slot.update(this.entity, level);
        }
    }
}
