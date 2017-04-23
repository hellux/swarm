package se.liu.ida.noahe116.tddd78.swarm.game.components;

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

    //TODO maybe include GameLevel, getting it through through entity is bad
    public abstract void update();
}
