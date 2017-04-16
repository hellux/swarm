package se.liu.ida.noahe116.tddd78.swarm.game;

import java.util.function.Consumer;
import java.util.AbstractMap;
import java.util.EnumMap;

import se.liu.ida.noahe116.tddd78.swarm.common.Vector2D;
import se.liu.ida.noahe116.tddd78.swarm.game.weapons.*;
import se.liu.ida.noahe116.tddd78.swarm.game.components.*;

public final class EntityCreator {
    private static final AbstractMap<EntityType, Consumer<Entity>> CREATORS =
        new EnumMap<>(EntityType.class);

    static {
        CREATORS.put(EntityType.PLAYER, EntityCreator::createPlayer);
        CREATORS.put(EntityType.ASTEROID, EntityCreator::createAsteroid);
    }

    private EntityCreator() {}

    public static Entity create(EntityType type) {
        Entity e = new Entity(type);
        CREATORS.get(type).accept(e);
        return e;
    }

    private static void createAsteroid(Entity e) {
        PositionComponent pc = new PositionComponent();
        pc.setActive(false);
        e.add(pc);
        e.add(new HealthComponent(100));

        CollisionComponent cc = new CollisionComponent(200);
        cc.setKnockback(true);
        e.add(cc);
    }

    private static void createPlayer(Entity e) {
        e.add(new PositionComponent(new Vector2D(301, 0)));
        e.add(new HealthComponent(1, 100));
        

        WeaponHandlerComponent wh = new WeaponHandlerComponent(2);
        wh.add(ProjectileWeaponCreator.get(WeaponType.QUAD));
        e.add(wh);

        CollisionComponent cc = new CollisionComponent(100);
        cc.setKnockback(true);
        e.add(cc);

        e.add(new PlayerComponent(e));
    }
}
