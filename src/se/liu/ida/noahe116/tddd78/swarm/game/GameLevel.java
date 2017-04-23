package se.liu.ida.noahe116.tddd78.swarm.game;

import java.util.List;
import java.util.LinkedList;

import se.liu.ida.noahe116.tddd78.swarm.game.components.*;
import se.liu.ida.noahe116.tddd78.swarm.common.Vector2D;

public class GameLevel {
    private Entity player;
    private final List<Entity> entities;
    private final double size;
    private final GameLevelSpec spec;

    public GameLevel(int size) {
        this.size = size;
        this.player = EntityCreator.create(EntityType.PLAYER);
        this.entities = new LinkedList<>();
        this.add(this.player);
        this.add(EntityCreator.create(EntityType.ASTEROID));
        //this.add(EntityCreator.create(CollectibleType.SPREAD));
        this.add(EntityCreator.create(CollectibleType.QUAD));
        this.spec = null;
    }

    public GameLevel(GameLevelSpec spec) {
        this.size = spec.getSize();
        this.entities = spec.getStartEntities();

        this.spec = spec;
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
                    ent1.collideWith(ent2, intersection);
                    ent2.collideWith(ent1, intersection.flipped());
                }
            }
        }
    }

    public void update() {
        this.checkCollisions();
        this.updateEntities();
    }

    public double getSize() {
        return this.size;
    }

    public void add(Entity e) {
        e.setGameLevel(this);
        this.entities.add(e);
    }
    
    public Entity getPlayer() {
        return this.player;
    }

    public List<Entity> getEntities() {
        return this.entities;
    }
}
