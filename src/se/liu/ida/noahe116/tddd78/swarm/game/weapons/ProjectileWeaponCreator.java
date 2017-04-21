package se.liu.ida.noahe116.tddd78.swarm.game.weapons;

import java.util.EnumMap;
import java.util.AbstractMap;

import se.liu.ida.noahe116.tddd78.swarm.common.Vector2D;
import se.liu.ida.noahe116.tddd78.swarm.game.*;

/**
 * Create projectile weapon instances.
 * Weapons are created at program start.
 * Pointers to weapons are retrieved by weapon type with the get method.
 **/
public final class ProjectileWeaponCreator {
    private static final int DEF_SPD = 30;
    private static final int SPR_SPD = 80;
    private static final int SPR_SPR = 25;
    private static final int RED_SPD = 50;
    private static final int QAD_SPD = 80;

    @SuppressWarnings({ "unchecked", "serial", "rawtypes"})
    private static final AbstractMap<WeaponType, ProjectileWeapon> WEAPONS =
        new EnumMap(WeaponType.class) {{
            put(WeaponType.DEFAULT, new ProjectileWeapon(
                WeaponType.DEFAULT,
                EntityType.PROJECTILE_DEFAULT,
                3, 10, 1,
                new Vector2D[]{new Vector2D(52, 78), new Vector2D(52, -78)},
                new Vector2D[]{new Vector2D(DEF_SPD, 0), new Vector2D(DEF_SPD, 0)}
            ));
            put(WeaponType.SPREAD, new ProjectileWeapon(
                WeaponType.SPREAD,
                EntityType.PROJECTILE_SPREAD,
                2, 10, 2,
                new Vector2D[]{new Vector2D(72, 74), new Vector2D(72, -74), 
                               new Vector2D(72, 80), new Vector2D(72, -80),
                               new Vector2D(72, 86), new Vector2D(72, -86)},
                new Vector2D[]{new Vector2D(SPR_SPD, -SPR_SPR), new Vector2D(SPR_SPD, SPR_SPR),
                               new Vector2D(SPR_SPD, 0), new Vector2D(SPR_SPD, 0),
                               new Vector2D(SPR_SPD, SPR_SPR), new Vector2D(SPR_SPD, -SPR_SPR)}
            ));
            put(WeaponType.RED_LASER, new ProjectileWeapon(
                WeaponType.RED_LASER,
                EntityType.PROJECTILE_RED_LASER,
                4, 50, 2,
                new Vector2D[]{new Vector2D(54, 79), new Vector2D(54, -79)},
                new Vector2D[]{new Vector2D(RED_SPD, 0), new Vector2D(RED_SPD, 0)}
            ));
            put(WeaponType.QUAD, new ProjectileWeapon(
                WeaponType.QUAD,
                EntityType.PROJECTILE_QUAD,
                2, 40, 2,
                new Vector2D[]{new Vector2D(42, 56), new Vector2D(42, -56),
                               new Vector2D(42, 79), new Vector2D(42, -79)},
                new Vector2D[]{new Vector2D(QAD_SPD, 0), new Vector2D(QAD_SPD, 0),
                               new Vector2D(QAD_SPD, 0), new Vector2D(QAD_SPD, 0)}
            ));
        }};

    private ProjectileWeaponCreator() {}

    /**
     * Return a ProjectileWeapon of a specific type.
     **/
    public static ProjectileWeapon get(WeaponType type) {
        if (!WEAPONS.containsKey(type)) {
            return WEAPONS.get(WeaponType.DEFAULT);
        }
        return WEAPONS.get(type);
    }
}
