package se.liu.ida.noahe116.tddd78.swarm.game;

import java.util.function.Consumer;
import java.util.AbstractMap;
import java.util.EnumMap;

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
        e.add(new PositionComponent());
        e.add(new HealthComponent(30));
    }

    private static void createPlayer(Entity e) {
        e.add(new PositionComponent());
        e.add(new HealthComponent(1, 100));
        e.add(new PlayerComponent(e));
    }
    
}
