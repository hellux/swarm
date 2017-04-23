package se.liu.ida.noahe116.tddd78.swarm.game;

import java.util.function.Consumer;
import java.util.AbstractMap;
import java.util.EnumMap;
import java.util.logging.*;

import se.liu.ida.noahe116.tddd78.swarm.common.Vector2D;
import se.liu.ida.noahe116.tddd78.swarm.game.weapons.*;
import se.liu.ida.noahe116.tddd78.swarm.game.components.*;

public final class EntityCreator {
    private static final Logger LOGGER = Logger.getLogger(EntityCreator.class.getName());

    @SuppressWarnings({"unchecked", "serial", "rawtypes"})
    private static final AbstractMap<CollectibleType, EntityType> COLLECTIBLES =
        new EnumMap(CollectibleType.class) {{
            put(CollectibleType.RED_LASER, EntityType.COLLECTIBLE_RED_LASER);
            put(CollectibleType.SPREAD, EntityType.COLLECTIBLE_SPREAD);
            put(CollectibleType.QUAD, EntityType.COLLECTIBLE_QUAD);
        }};

    private static final AbstractMap<EntityType, Consumer<Entity>> CREATORS =
        new EnumMap<>(EntityType.class);


    static {
        CREATORS.put(EntityType.PLAYER, EntityCreator::createPlayer);
        CREATORS.put(EntityType.ASTEROID, EntityCreator::createAsteroid);
    }

    private EntityCreator() {}

    /**
     * Create an entity representing a specific entity type.
     **/
    public static Entity create(EntityType type) {
        Entity e = new Entity(type);
        if (CREATORS.containsKey(type)) {
            CREATORS.get(type).accept(e);
            return e;
        } else {
            LOGGER.log(Level.WARNING, "no creator for " + type);
            return null;
        }
    }

    /**
     * Create an entity representing a collectible of a specific collectible type.
     **/
    public static Entity create(CollectibleType type) {
        if (COLLECTIBLES.containsKey(type)) {
            Collectible coll = CollectibleCreator.get(type);
            if (coll != null) {
                Entity e = new Entity(COLLECTIBLES.get(type));
                createCollectible(e, coll);
                return e;
            }
        } else {
            LOGGER.log(Level.WARNING, "no entity type for collectible " + type);
        }
        return null;
    }

    private static void createCollectible(Entity e, Collectible coll) {
        PositionComponent pc = new PositionComponent();
        pc.setRotationalSpeed(0.1);
        pc.setRotation(Math.random()*2*Math.PI);
        e.add(pc);

        CollisionComponent cc = new CollisionComponent(40);
        cc.setKnockback(false);
        cc.setDamage(0);
        e.add(cc);

        e.add(new CollectibleComponent(coll));
    }

    private static void createAsteroid(Entity e) {
        PositionComponent pc = new PositionComponent(new Vector2D(-500, 0));
        pc.setActive(false);
        e.add(pc);
        e.add(new HealthComponent(100));

        CollisionComponent cc = new CollisionComponent(200);
        cc.setKnockback(true);
        e.add(cc);
    }

    private static void createPlayer(Entity e) {
        e.add(new HealthComponent(1, 100));
        e.add(new ThrustComponent());

        PositionComponent pc = new PositionComponent(new Vector2D(300, 0));
        pc.setDrag(true);
        e.add(pc);

        //TODO add weapons on pickup instead
        WeaponHandlerComponent wh = new WeaponHandlerComponent(2);
        wh.addUnlimited(WeaponType.DEFAULT, 0);
        wh.add(WeaponType.RED_LASER, 0);
        wh.add(WeaponType.SPREAD, 0);
        wh.add(WeaponType.QUAD, 0);
        e.add(wh);

        CollisionComponent cc = new CollisionComponent(100);
        cc.setKnockback(true);
        e.add(cc);

        e.add(new PlayerComponent());
    }
}
