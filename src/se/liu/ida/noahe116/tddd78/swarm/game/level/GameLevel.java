package se.liu.ida.noahe116.tddd78.swarm.game.level;

import java.util.Random;
import java.util.List;
import java.util.LinkedList;
import java.util.logging.*;

import se.liu.ida.noahe116.tddd78.swarm.game.entities.EnemyType;
import se.liu.ida.noahe116.tddd78.swarm.game.entities.Entity;
import se.liu.ida.noahe116.tddd78.swarm.game.entities.EntityCreator;
import se.liu.ida.noahe116.tddd78.swarm.game.entities.EntityType;
import se.liu.ida.noahe116.tddd78.swarm.game.components.*;
import se.liu.ida.noahe116.tddd78.swarm.common.*;

public class GameLevel {
    private static final Logger LOGGER = Logger.getLogger(GameLevel.class.getName());
    private static final Random RAND = new Random();

    private Entity player;
    private final List<Entity> entities = new LinkedList<>();
    private final double size;
    private final GameLevelSpec spec;

    private int enemySpawnCooldown = 0;
    private int wave = 0;
    private long tick = 0;
    private int enemyCount = 0;
    private ProbabilityMap<EnemyType> enemyProbabilites = null;

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

    private void updateWave() {
        if (this.wave < this.spec.getWaveCount()) {
            if (this.spec.getNextWaveTick(this.wave) <= this.tick) {
                this.wave++;
                this.enemyProbabilites = this.spec.getEnemyProbabilites(this.wave);
            }
        }

        if (this.enemySpawnCooldown > 0) {
            this.enemySpawnCooldown--;
        }
        else if (this.enemyCount < this.spec.getMaxEnemies(this.wave)) {
            this.spawn(EntityCreator.create(this.enemyProbabilites.get()));
            this.enemyCount++;
        }

        this.tick++;
    }

    private void updateEntities() {
        for (int i = entities.size()-1; i >= 0; i--) {
            Entity entity = this.entities.get(i);
            if (entity.isKilled()) {
                this.entities.remove(entity);
            }
            entity.update(this);
        }
    }

    //TODO improve and move to own class
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

                Vector2D intersection = cc1.intersection(cc2, this);
                if (intersection != null) {
                    ent1.collideWith(ent2, intersection);
                    ent2.collideWith(ent1, intersection.flipped());
                }
            }
        }
    }

    public void update() {
        this.updateWave();
        this.checkCollisions();
        this.updateEntities();
    }

    public double getSize() {
        return this.size;
    }

    public void add(Entity e) {
        this.entities.add(e);
    }
    
    public Entity getPlayer() {
        return this.player;
    }

    public List<Entity> getEntities() {
        return this.entities;
    }

    /**
     * Wrap a coordinate around the game level if it's on the other side relative to p2.
     * <pre> {@code
     *
     * P   :  position of this component 
     * P'  :  position of point to eventually be wrapped around
     * )(  :  pivot = (C+S/2) % S
     *
     * Cases:
     *
     *      |---P--------)(-----------|
     *      0                         S
     *
     *       P , P' < pivot   =>   P' =  P'
     *       P < pivot < P'   =>   P' -= S  (*)
     *
     *
     *      |--------)(-----------P---|
     *      0                         S
     *
     *       pivot < P , P'   =>   P' =  P'
     *       P' < pivot < P   =>   P' += S  (*)
     *
     * } </pre>
     * @param p1 position 
     * @param p2 position to eventually wrap around
     * @return same position but eventually wrapped around.
     **/
    private double wrapAround(double p1, double p2) {
        double pivot = Math2.floorMod(p1 + this.size/2, this.size);

        if (p1 < pivot && pivot < p2) {
            return p2 - this.size;
        } else if (p2 < pivot && pivot < p1) {
            return p2 + this.size;
        } else {
            return p2;
        }
    }
    
    /**
     * Eventually wrap around a position if it's on the other side of the level
     * @param pos1 a position
     * @param pos2 position to eventually wrap around
     * @return pos2 eventually wrapped around
     **/
    public Vector2D wrapAround(Vector2D pos1, Vector2D pos2) {
        return new Vector2D(
            this.wrapAround(pos1.x, pos2.x),
            this.wrapAround(pos1.y, pos2.y)
        );
    }
}
