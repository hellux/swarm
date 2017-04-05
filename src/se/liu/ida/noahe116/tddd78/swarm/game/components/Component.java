package se.liu.ida.noahe116.tddd78.swarm.game.components;

import se.liu.ida.noahe116.tddd78.swarm.game.Entity;

public abstract class Component {
    /**
     * The entity that the component is added to.
     **/
    protected Entity entity;
    
    public void setEntity(Entity entity) {
        this.entity = entity;
    }

}
