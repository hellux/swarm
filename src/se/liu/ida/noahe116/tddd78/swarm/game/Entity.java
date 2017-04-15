package se.liu.ida.noahe116.tddd78.swarm.game;

import se.liu.ida.noahe116.tddd78.swarm.game.components.Component;
import se.liu.ida.noahe116.tddd78.swarm.game.components.LiveComponent;

import java.util.HashMap;
import java.util.AbstractMap;

public class Entity {
    /**
     * Map of entity's components.
     * Key is class of component, value is instance.
     **/
    private final AbstractMap<Class<? extends Component>, Component> components = new HashMap<>();
    private final EntityType type;
    private Game game = null;
    private boolean killed;
    
    public Entity(EntityType type) {
        this.type = type;
    }

    /**
     * Add a component.
     * @param component component to be added
     * @return true if added, false if a component of the same class
     *         already exists.
     **/
    public boolean add(Component component) {
        if (this.has(component.getClass())) {
            return false;
        } else {
            this.components.put(component.getClass(), component);
            component.setEntity(this);
            return true;
        }
    }

    /**
     * Remove a component of a certain class.
     * @param componentClass class of component to be removed.
     * @return the removed component or null, if it doesn't exist.
     **/
    public <T extends Component> T remove(Class<T> componentClass) {
        // remove always returns instance of componentClass (or null)
        @SuppressWarnings("unchecked") 
        T oldComponent = (T) this.components.remove(componentClass);
        if (oldComponent != null) oldComponent.setEntity(null);
        return oldComponent;
    }

    @SuppressWarnings("unchecked") // key always represent type of value
    /**
     * Get a component of a certain class.
     * @param componentClass class of component to get.
     * @return the component or null, if it doesn't exist.
     **/
    public <T extends Component> T get(Class<T> componentClass) {
        return (T) this.components.get(componentClass);
    }

    /**
     * Check if entity has a component of a certain class.
     * @param componentClass class of component.
     * @return whether entity has component.
     **/
    public <T extends Component> boolean has(Class<T> componentClass) {
        return this.components.containsKey(componentClass);
    }

    /**
     * Update all live components that are active.
     **/
    public void update() {
        for (Component component : this.components.values()) {
            if (component instanceof LiveComponent) {
                LiveComponent liveComponent = (LiveComponent) component;
                if (liveComponent.isActive()) {
                    liveComponent.update();
                }
            }
        }
    }
    
    /**
     * Flag the entity as killed.
     **/
    public void kill() {
        this.killed = true;
    }

    public EntityType getType() {
        return this.type;
    }

    public boolean isKilled() {
        return this.killed;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return this.game;
    }

    @Override
    public String toString() {
        return "ent_" + this.type + "_" + this.hashCode();
    }

    /**
     * Replace a component of the same class.
     * FIXME: Will not compile because 'inference variable T1 has
     * incompatible bounds'. The type that returns from remove
     * doesn't seem to be recognized as the same as T.
     * @param component new component.
     * @return old component if it existed, otherwise null.
    /**
    public <T extends Component> T replace(T component) {
        T oldComponent = this.remove(component.getClass());
        this.add(component);
        return oldComponent;
    }
    **/
}
