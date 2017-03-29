package se.liu.ida.noahe116.tddd78.swarm.game;

public final class EntityCreator {
    private EntityCreator() {}

    public static Entity createPlayer() {
        Entity entity = new Entity(EntityType.PLAYER);
        entity.add(new PositionComponent());
        entity.add(new HealthComponent(1, 100));
        entity.add(new PlayerComponent(entity));
        return entity;
    }
}
