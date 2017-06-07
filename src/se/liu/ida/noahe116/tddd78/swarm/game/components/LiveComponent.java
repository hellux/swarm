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

    @Override
    public int compareTo(LiveComponent lc) {
        return Integer.compare(this.updatePriority, lc.updatePriority);
    }

    /**
     * LiveComponents are considered equal if they have the same update priority
     * @param o object to compare with
     * @return if object is considered equal to this object
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (this.getClass() != o.getClass()) return false;

        LiveComponent lc = (LiveComponent) o;
        return this.updatePriority == lc.updatePriority;
    }

    /**
     * Update the component.
     * Will be run every tick of the game if the component is active.
     * @param level the game level the component is part of
     **/
    public abstract void update(GameLevel level);
}
