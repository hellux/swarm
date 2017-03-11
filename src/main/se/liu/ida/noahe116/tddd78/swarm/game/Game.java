package se.liu.ida.noahe116.tddd78.swarm.game;

public class Game {
    Player player;
    
    public Game() {
        this.player = new Player();
    }

    public void tick() {
        this.player.tick();
    }
}
