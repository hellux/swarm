package se.liu.ida.noahe116.tddd78.swarm.game.weapons;

import java.util.EnumMap;
import java.util.AbstractMap;

import se.liu.ida.noahe116.tddd78.swarm.common.Vector2D;

/**
 * Create projectile weapon instances.
 * Weapons are created at program start.
 * Pointers to weapons are retrieved by weapon type with the get method.
 **/
public final class ProjectileWeaponCreator {
    @SuppressWarnings({"rawtypes", "unchecked", "serial"})
    private static final AbstractMap<WeaponType, ProjectileWeapon> WEAPONS =
        new EnumMap(WeaponType.class) {{
            put(WeaponType.DEFAULT, new ProjectileWeapon(
                3, 
                10,
                1,
                new Vector2D[]{new Vector2D(52, 78), new Vector2D(52, -78)},
                new Vector2D[]{new Vector2D(25, 0), new Vector2D(25, 0)}
            ));
        }};

    /**
     * Return a ProjectileWeapon of a specific type.
     **/
    public static final ProjectileWeapon get(WeaponType type) {
        if (!WEAPONS.containsKey(type)) {
            return WEAPONS.get(WeaponType.DEFAULT);
        }
        return WEAPONS.get(type);
    }
}
