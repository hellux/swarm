package se.liu.ida.noahe116.tddd78.swarm.game.entities;

import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.AbstractMap;
import java.util.EnumMap;
import java.util.logging.*;

import se.liu.ida.noahe116.tddd78.swarm.common.Vector2D;
import se.liu.ida.noahe116.tddd78.swarm.game.collectibles.Collectible;
import se.liu.ida.noahe116.tddd78.swarm.game.collectibles.CollectibleCreator;
import se.liu.ida.noahe116.tddd78.swarm.game.collectibles.CollectibleType;
import se.liu.ida.noahe116.tddd78.swarm.game.weapons.*;
import se.liu.ida.noahe116.tddd78.swarm.game.components.*;

//TODO make less messy, maybe builder method that handles component initialization
// values should be moved to xml files or similar
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
    
    private static final AbstractMap<EnemyType, Supplier<Entity>> ENEMY_CREATORS =
        new EnumMap<>(EnemyType.class);

    static {
        CREATORS.put(EntityType.PLAYER, EntityCreator::createPlayer);
        CREATORS.put(EntityType.ASTEROID, EntityCreator::createAsteroid);

        ENEMY_CREATORS.put(EnemyType.CLAG_BOT, EntityCreator::createClagBot);
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

    public static Entity create(EnemyType type) {
        if (ENEMY_CREATORS.containsKey(type)) {
            return ENEMY_CREATORS.get(type).get();
        } else {
            LOGGER.log(Level.WARNING, "no creator for " + type);
            return null;
        }
    }

    private static void createCollectible(Entity e, Collectible coll) {
        PositionComponent pc = new PositionComponent();
        pc.setRotationalSpeed(0.1);
        pc.setRotation(Math.random()*2*Math.PI);
        e.add(pc);
        
        e.add(new CollisionComponent(40));
        e.add(new CollectibleComponent(coll));
    }

    private static void createAsteroid(Entity e) {
        PositionComponent pc = new PositionComponent(new Vector2D(-500, 0));
        pc.setActive(false);
        e.add(pc);

        CollisionComponent cc = new CollisionComponent(175);
        cc.setDamage(15);
        e.add(cc);

        e.add(new HealthComponent(100));
        e.add(new KnockbackComponent());
    }

    private static void createPlayer(Entity e) {
        e.add(new HealthComponent(1, 100));
        e.add(new ThrustComponent());

        PositionComponent pc = new PositionComponent(new Vector2D(300, 0));
        pc.setDrag(true);
        e.add(pc);

        CollisionComponent cc = new CollisionComponent(102);
        cc.setDamage(15);
        e.add(cc);

        WeaponHandlerComponent wh = new WeaponHandlerComponent(2);
        wh.addUnlimited(WeaponType.DEFAULT, 0);
        wh.add(WeaponType.RED_LASER, 0);
        wh.add(WeaponType.SPREAD, 0);
        wh.add(WeaponType.QUAD, 0);
        e.add(wh);

        e.add(new PlayerComponent());
    }

    private static Entity createClagBot() {
        Entity e = new Entity(EntityType.ENEMY_CLAG_BOT);
        
        e.add(new HealthComponent(20));
        e.add(new PositionComponent());
        e.add(new ClagBotComponent());

        e.add(new CollisionComponent(206));

        ThrustComponent tc = new ThrustComponent();
        tc.setThrustPower(0.05);
        e.add(tc);

        return e;
    }
}
