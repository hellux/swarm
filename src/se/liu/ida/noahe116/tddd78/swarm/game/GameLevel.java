package se.liu.ida.noahe116.tddd78.swarm.game;

import java.util.Random;
import java.util.List;
import java.util.LinkedList;
import java.util.logging.*;

import se.liu.ida.noahe116.tddd78.swarm.game.components.*;
import se.liu.ida.noahe116.tddd78.swarm.common.Vector2D;

public class GameLevel {
    private static final Logger LOGGER = Logger.getLogger(GameLevel.class.getName());
    private static final Random RAND = new Random();

    private Entity player;
    private final List<Entity> entities = new LinkedList<>();
    private final double size;
    private final GameLevelSpec spec;

    public GameLevel(GameLevelSpec spec) {
        this.size = spec.getSize();
        this.player = EntityCreator.create(EntityType.PLAYER);
        this.add(this.player);
        this.spawn(spec.createStartEntities());

        this.spec = spec;
    }

    /**
     * Place an entity randomly on the map.
     * TODO avoid collisions
     **/
    private void spawn(Entity e) {
        PositionComponent pc = e.get(PositionComponent.class);
        if (pc == null) {
            LOGGER.log(Level.WARNING, "can't spawn entity, no poscomp: " + e);
        } else {
            pc.setPosition(new Vector2D(RAND.nextInt((int)this.size),
                                        RAND.nextInt((int)this.size)));
            this.add(e);
        }
    }

    private void spawn(List<Entity> entities) {
        for (Entity e : entities) {
            this.spawn(e);
        }
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
