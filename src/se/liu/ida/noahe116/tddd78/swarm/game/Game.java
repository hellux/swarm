package se.liu.ida.noahe116.tddd78.swarm.game;

import java.util.List;
import java.util.LinkedList;

import se.liu.ida.noahe116.tddd78.swarm.game.components.*;
import se.liu.ida.noahe116.tddd78.swarm.common.Vector2D;

public class Game {
    private Entity player;
    private final List<Entity> entities = new LinkedList<>();

    public Game() {
        this.player = EntityCreator.create(EntityType.PLAYER);
        this.entities.add(this.player);
        this.entities.add(EntityCreator.create(EntityType.ASTEROID));
    }

    public Entity getPlayer() {
        return this.player;
    }

    public List<Entity> getEntities() {
        return this.entities;
    }

    public void update() {
        this.checkCollisions();
        this.updateEntities();
    }

    private void updateEntities() {
        for (Entity entity : this.entities) {
            if (entity.isKilled()) {
                this.entities.remove(entities);
            }
            entity.update();
        }
    }

    private void checkCollisions() {
        for (int e1 = 0; e1 < this.entities.size(); e1++) {
            CollisionComponent cc1 = this.entities.get(e1).get(CollisionComponent.class);
            if (cc1 == null) continue;
            for (int e2 = e1+1; e2 < this.entities.size(); e2++) {
                CollisionComponent cc2 = this.entities.get(e2).get(CollisionComponent.class);
                if (cc2 == null) continue;
                Vector2D intersection = cc1.collidesWith(cc2);
                if (intersection != null) {
                    cc1.collideWith(cc2, intersection);
                    cc2.collideWith(cc1, Vector2D.multiply(intersection, -1));
                }
            }
        }
    }
}
