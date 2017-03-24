package se.liu.ida.noahe116.tddd78.swarm.game;

public abstract class LiveComponent extends Component {
    protected boolean active;

    protected LiveComponent(boolean active) {
        this.active = active;
    }
    
    protected LiveComponent() {
        this.active = true;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public abstract void update();
}
