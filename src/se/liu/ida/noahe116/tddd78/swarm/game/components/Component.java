package se.liu.ida.noahe116.tddd78.swarm.game.components;

import se.liu.ida.noahe116.tddd78.swarm.game.entities.Entity;
import se.liu.ida.noahe116.tddd78.swarm.common.Vector2D;

public abstract class Component {
    /**
     * The entity that the component is added to.
     **/
    protected Entity entity = null;
    
    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return this.entity;
    }

    public void collideWith(Entity e, Vector2D intersection) {

    }
}
