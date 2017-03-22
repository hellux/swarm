package se.liu.ida.noahe116.tddd78.swarm.game;

public abstract class Component {
    /**
     * If the part needs to be updated every tick
     **/
    protected boolean active;
    
    /**
     * The entity that the component is added to.
     **/
    protected Entity entity;
    
    public Component(boolean active) {
        this.active = active;
    }

    public Component() {
        this(false);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return this.entity;
    }

    public void update() {

    }
}
