package se.liu.ida.noahe116.tddd78.swarm.game;

public class HealthComponent extends Component {
    private int healthPoints;
    private int shieldStrength;

    public HealthComponent(int hp, int shield) {
        this.healthPoints = hp;
        this.shieldStrength = shield;
    }

    public HealthComponent(int hp) {
        this(hp, 0);
    }

    public void hurt(int damage) {
        if (this.shieldStrength > 0) {
            this.shieldStrength -= damage;
        } else {
            this.healthPoints -= damage;
        }
        // die if healthPoints <= 0;
    }
    
    public void update() {};
}
