package se.liu.ida.noahe116.tddd78.swarm.game;

public abstract class Component {
    /**
     * If the part needs to be updated every tick
     **/
    protected boolean isActive = false;
    
    /**
     * The entity that the component is added to.
     **/
    protected Entity entity;

    public boolean isActive() {
        return isActive;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return this.entity;
    }

    public abstract void update();
}
