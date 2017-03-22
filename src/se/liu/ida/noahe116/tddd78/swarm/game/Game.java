package se.liu.ida.noahe116.tddd78.swarm.game;

import java.util.List;
import java.util.LinkedList;

public class Game {
    private Entity player;
    private final List<Entity> entities = new LinkedList<>();

    public Game() {
        this.player = EntityCreator.createPlayer();
        this.entities.add(this.player);
    }

    public Entity getPlayer() {
        return this.player;
    }

    public List<Entity> getEntities() {
        return this.entities;
    }

    public void update() {
        for (Entity entity : this.entities) {
            entity.update();
        }
    }
}
