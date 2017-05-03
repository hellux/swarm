package se.liu.ida.noahe116.tddd78.swarm.game.components;

import se.liu.ida.noahe116.tddd78.swarm.game.entities.Entity;

/**
 * Component that can be attached to an entity.
 * Used to avoid hierarchy issues with entities.
 * Components with properties and behaviour can
 * easily be (de)attached to/from entities. 
 **/
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
}
