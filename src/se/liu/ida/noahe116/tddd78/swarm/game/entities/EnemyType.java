package se.liu.ida.noahe116.tddd78.swarm.game.entities;

/**
 * Types of enemies with corresponding entity types.
 **/
public enum EnemyType {
    CLAG_BOT(EntityType.ENEMY_CLAG_BOT),
    CLAG_SCOUT(EntityType.ENEMY_CLAG_SCOUT);

    /**
     * EntityType of enemy
     */
    public final EntityType entityType;

    private EnemyType(EntityType type) {
        this.entityType = type;
    }
}
