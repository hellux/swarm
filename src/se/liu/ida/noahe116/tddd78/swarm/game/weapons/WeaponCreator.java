package se.liu.ida.noahe116.tddd78.swarm.game.weapons;

import java.util.EnumMap;
import java.util.AbstractMap;
import java.util.logging.*;

import se.liu.ida.noahe116.tddd78.swarm.common.Vector2D;
import se.liu.ida.noahe116.tddd78.swarm.game.*;

/**
 * Create weapon instances.
 * Weapons are created at program start.
 * Weapons are retrieved by WeaponType with the get method.
 **/
public final class WeaponCreator {
    private static final Logger LOGGER = Logger.getLogger(WeaponCreator.class.getName());

    private static final int DEF_SPD = 30;
    private static final int DEF_DMG = 5;
    private static final int SPR_SPD = 80;
    private static final int SPR_DMG = 10;
    private static final int SPR_SPR = 25;
    private static final int RED_SPD = 50;
    private static final int RED_DMG = 10;
    private static final int QAD_SPD = 80;
    private static final int QAD_DMG = 40;

    @SuppressWarnings({"unchecked", "serial", "rawtypes"})
    private static final AbstractMap<WeaponType, Weapon> WEAPONS =
        new EnumMap(WeaponType.class) {{
            put(WeaponType.DEFAULT, new ProjectileWeapon(
                WeaponType.DEFAULT,
                EntityType.PROJECTILE_DEFAULT,
                3, DEF_DMG, 1,
                new Vector2D[]{new Vector2D(52, 78), new Vector2D(52, -78)},
                new Vector2D[]{new Vector2D(DEF_SPD, 0), new Vector2D(DEF_SPD, 0)}
            ));
            put(WeaponType.SPREAD, new ProjectileWeapon(
                WeaponType.SPREAD,
                EntityType.PROJECTILE_SPREAD,
                2, SPR_DMG, 2,
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
                4, RED_DMG, 2,
                new Vector2D[]{new Vector2D(54, 79), new Vector2D(54, -79)},
                new Vector2D[]{new Vector2D(RED_SPD, 0), new Vector2D(RED_SPD, 0)}
            ));
            put(WeaponType.QUAD, new ProjectileWeapon(
                WeaponType.QUAD,
                EntityType.PROJECTILE_QUAD,
                2, QAD_DMG, 2,
                new Vector2D[]{new Vector2D(42, 56), new Vector2D(42, -56),
                               new Vector2D(42, 79), new Vector2D(42, -79)},
                new Vector2D[]{new Vector2D(QAD_SPD, 0), new Vector2D(QAD_SPD, 0),
                               new Vector2D(QAD_SPD, 0), new Vector2D(QAD_SPD, 0)}
            ));
        }};

    private WeaponCreator() {}

    /**
     * Return a ProjectileWeapon of a specific type.
     **/
    public static Weapon get(WeaponType type) {
        if (!WEAPONS.containsKey(type)) {
            LOGGER.log(Level.WARNING, "can't create weapon of type " + type);
            return WEAPONS.get(WeaponType.DEFAULT);
        }
        return WEAPONS.get(type);
    }
}
