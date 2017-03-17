package se.liu.ida.noahe116.tddd78.swarm.game;

import java.util.List;
import java.util.LinkedList;

public class Game {
    Player player;
    List<Entity> entities = new LinkedList<>();
    
    public Game() {
        this.player = new Player();
        this.entities.add(this.player);
    }

    public Player getPlayer() {
        return this.player;
    }

    public List<Entity> getEntities() {
        return this.entities;
    }

    public void tick() {
        this.player.tick();
    }
}
