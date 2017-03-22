package se.liu.ida.noahe116.tddd78.swarm.game;

import java.util.HashMap;
import java.util.AbstractMap;

public class Entity {
    private final AbstractMap<Class<? extends Component>, Component> components =
        new HashMap<>();
    
    /** Add a component.
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

    /** Replace a component of the same class.
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

    /** Remove a component of a certain class.
     * @param componentClass class of component to be removed.
     * @return the removed component or null, if it doesn't exist.
     **/
    public <T extends Component> T remove(Class<T> componentClass) {
        T oldComponent = (T) this.components.remove(componentClass);
        if (oldComponent != null) oldComponent.setEntity(null);
        return oldComponent;
    }

    /** Get a component of a certain class.
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
     * Update all components that are active.
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
}
