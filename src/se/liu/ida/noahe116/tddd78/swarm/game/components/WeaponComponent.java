package se.liu.ida.noahe116.tddd78.swarm.game.components;

public class WeaponComponent extends Component {
    private int ammunition = 0;

    public void fire() {

    }

    public void addAmmunition(int ammo) {
        this.ammunition += ammo;
    }

    public int getAmmunition() {
        return this.ammunition;
    }
}
