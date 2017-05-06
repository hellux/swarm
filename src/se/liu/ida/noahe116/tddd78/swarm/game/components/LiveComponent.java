package se.liu.ida.noahe116.tddd78.swarm.game.components;

import se.liu.ida.noahe116.tddd78.swarm.game.level.*;

/**
 * Components that needs updating every tick
 **/
public abstract class LiveComponent extends Component implements Comparable<LiveComponent> {
    protected boolean active;
    private final int updatePriority;

    protected LiveComponent(int updatePriority) {
        this.updatePriority = updatePriority;
        this.active = true;
    }

    protected LiveComponent() {
        this(0);
    }

    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }

    public int compareTo(LiveComponent lc) {
        System.out.println(lc.updatePriority +" "+ this.updatePriority);
        return Integer.compare(this.updatePriority, lc.updatePriority);
    }
    
    /**
     * Update the component.
     * Will be run every tick of the game if the component is active.
     * @param level the game level the component is part of
     **/
    public abstract void update(GameLevel level);
}
