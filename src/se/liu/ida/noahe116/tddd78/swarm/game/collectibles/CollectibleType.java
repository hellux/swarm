package se.liu.ida.noahe116.tddd78.swarm.game.collectibles;

import se.liu.ida.noahe116.tddd78.swarm.game.entities.EntityType;

public enum CollectibleType {
    SHIELD(EntityType.COLLECTIBLE_SHIELD),
    SHIP(EntityType.COLLECTIBLE_SHIP),
    CRYSTAL(EntityType.COLLECTIBLE_CRYSTAL),
    RED_LASER(EntityType.COLLECTIBLE_RED_LASER),
    SPREAD(EntityType.COLLECTIBLE_SPREAD),
    QUAD(EntityType.COLLECTIBLE_QUAD);

    public final EntityType entityType;

    private CollectibleType(EntityType type) {
        this.entityType = type;
    }
}
