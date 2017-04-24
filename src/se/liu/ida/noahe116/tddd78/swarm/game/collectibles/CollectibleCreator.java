package se.liu.ida.noahe116.tddd78.swarm.game.collectibles;

import java.util.EnumMap;
import java.util.AbstractMap;
import java.util.logging.*;

import se.liu.ida.noahe116.tddd78.swarm.game.weapons.*;

public final class CollectibleCreator {
    private static final Logger LOGGER =
        Logger.getLogger(CollectibleCreator.class.getName());

    @SuppressWarnings({"unchecked", "serial", "rawtypes"}) // IDEA: rawtypes is not reduntant
    private static final AbstractMap<CollectibleType, Collectible> COLLECTIBLES =
        new EnumMap(CollectibleType.class) {{
            put(CollectibleType.RED_LASER, new WeaponCollectible(WeaponType.RED_LASER, 30));
            put(CollectibleType.SPREAD, new WeaponCollectible(WeaponType.SPREAD, 50));
            put(CollectibleType.QUAD, new WeaponCollectible(WeaponType.QUAD, 25));
        }};

    public static Collectible get(CollectibleType type) {
        if (!COLLECTIBLES.containsKey(type)) {
            LOGGER.log(Level.WARNING, "can't create collectible of type " + type);
            return null;
        }
        return COLLECTIBLES.get(type);
    }
}
