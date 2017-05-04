package se.liu.ida.noahe116.tddd78.swarm.game.entities;

import java.util.function.Consumer;
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
/**
 * Create entities of specific types.
 **/
public final class EntityCreator {
    private static final Logger LOGGER = Logger.getLogger(EntityCreator.class.getName());

    private static final AbstractMap<EntityType, Consumer<Entity>> CREATORS =
        new EnumMap<>(EntityType.class);
    
    private static final AbstractMap<EnemyType, Consumer<Entity>> ENEMY_CREATORS =
        new EnumMap<>(EnemyType.class);

    static {
        CREATORS.put(EntityType.PLAYER, EntityCreator::createPlayer);
        CREATORS.put(EntityType.ASTEROID, EntityCreator::createAsteroid);
        CREATORS.put(EntityType.TELEPORTER, EntityCreator::createTeleporter);

        ENEMY_CREATORS.put(EnemyType.CLAG_BOT, EntityCreator::createClagBot);
    }

    private EntityCreator() {}

    /**
     * Create an entity representing a specific entity type.
     * @param type type of entity to create
     * @return entity of given type
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
     * @param type type of collectible to create
     * @return entity of given collectible type
     **/
    public static Entity create(CollectibleType type) {
        Collectible coll = CollectibleCreator.get(type);
        if (coll != null) {
            Entity e = new Entity(type.entityType);
            createCollectible(e, coll);
            return e;
        }
        return null;
    }

    /**
     * Create an entity representing an enemy of a specific type.
     * @param type type of enemy
     * @return entity of given enemy type
     **/
    public static Entity create(EnemyType type) {
        if (ENEMY_CREATORS.containsKey(type)) {
            Entity e = new Entity(type.entityType);
            ENEMY_CREATORS.get(type).accept(e);
            return e;
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
        
        CollisionComponent cc = new CollisionComponent(40);
        cc.whitelist(EntityType.PLAYER);
        e.add(cc);
        
        e.add(new CollectibleComponent(coll));
    }

    private static void createAsteroid(Entity e) {
        PositionComponent pc = new PositionComponent();
        pc.setActive(false);
        e.add(pc);

        CollisionComponent cc = new CollisionComponent(175);
        cc.setDamage(15);
        e.add(cc);

        e.add(new HealthComponent(100));
        e.add(new KnockbackComponent());
    }

    private static void createTeleporter(Entity e) {
        PositionComponent pc = new PositionComponent();
        pc.setRotationalSpeed(0.01);
        pc.setRotation(Math.random()*2*Math.PI);
        e.add(pc);

        CollisionComponent cc = new CollisionComponent(113);
        cc.whitelist(EntityType.PLAYER);
        e.add(cc);

        e.add(new TeleporterComponent());
    }

    private static void createPlayer(Entity e) {
        HealthComponent hc = new HealthComponent(1, 200);
        hc.addExtraLives(2);
        e.add(hc);

        e.add(new ThrustComponent());

        PositionComponent pc = new PositionComponent();
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

    private static void createClagBot(Entity e) {
        e.add(new HealthComponent(35));
        e.add(new ClagBotComponent());
        e.add(new KnockbackComponent());

        CollisionComponent cc = new CollisionComponent(103);
        cc.setDamage(40);
        e.add(cc);

        ThrustComponent tc = new ThrustComponent();
        tc.setThrustPower(0.05);
        e.add(tc);
    }
}
