package se.liu.ida.noahe116.tddd78.swarm.game.components;

import se.liu.ida.noahe116.tddd78.swarm.game.level.*;

/**
 * Components that needs updating every tick
 **/
public abstract class LiveComponent extends Component {
    protected boolean active;

    protected LiveComponent() {
        this.active = true;
    }

    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Update the component.
     * Will be run every tick of the game if the component is active.
     * @param level the game level the component is part of
     **/
    public abstract void update(GameLevel level);
}
