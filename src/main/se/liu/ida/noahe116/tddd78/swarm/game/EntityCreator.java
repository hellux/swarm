package se.liu.ida.noahe116.tddd78.swarm.game;

public final class EntityCreator {
    private EntityCreator() {};

    public static Entity createPlayer() {
        Entity entity = new Entity();
        entity.add(new PlayerComponent());
        entity.add(new PositionComponent());
        return entity;
    }
}
