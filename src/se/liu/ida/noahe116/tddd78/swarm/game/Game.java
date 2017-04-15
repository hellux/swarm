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
        this.add(this.player);
        this.add(EntityCreator.create(EntityType.ASTEROID));
    }

    public Entity getPlayer() {
        return this.player;
    }

    public List<Entity> getEntities() {
        return this.entities;
    }

    private void updateEntities() {
        for (int i = entities.size()-1; i >= 0; i--) {
            Entity entity = this.entities.get(i);
            if (entity.isKilled()) {
                this.entities.remove(entity);
            }
            entity.update();
        }
    }

    private void checkCollisions() {
        for (int e1 = 0; e1 < this.entities.size(); e1++) {
            Entity ent1 = this.entities.get(e1);
            CollisionComponent cc1 = ent1.get(CollisionComponent.class);
            if (cc1 == null) continue;

            for (int e2 = e1+1; e2 < this.entities.size(); e2++) {
                Entity ent2 = this.entities.get(e2);
                if (ent1.getType() == ent2.getType()) continue;
                CollisionComponent cc2 = ent2.get(CollisionComponent.class);
                if (cc2 == null) continue;

                Vector2D intersection = cc1.intersection(cc2);
                if (intersection != null) {
                    cc1.collideWith(cc2, intersection);
                    cc2.collideWith(cc1, Vector2D.multiply(intersection, -1));
                }
            }
        }
    }

    public void add(Entity e) {
        e.setGame(this);
        this.entities.add(e);
    }
    
    public void update() {
        this.checkCollisions();
        this.updateEntities();
    }
}
