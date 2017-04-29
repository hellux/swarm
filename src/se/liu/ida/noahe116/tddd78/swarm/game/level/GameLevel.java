package se.liu.ida.noahe116.tddd78.swarm.game.level;

import java.util.Random;
import java.util.List;
import java.util.LinkedList;
import java.util.logging.*;

import se.liu.ida.noahe116.tddd78.swarm.game.entities.*;
import se.liu.ida.noahe116.tddd78.swarm.game.components.*;
import se.liu.ida.noahe116.tddd78.swarm.common.*;

public class GameLevel {
    private static final Logger LOGGER = Logger.getLogger(GameLevel.class.getName());

    private final Entity player;

    private final List<Entity> entities = new LinkedList<>();
    private final double size;
    private final GameLevelSpec spec;

    private int enemySpawnCooldown = 0;
    private int wave = 0;
    private long tick = 0;
    private int enemyCount = 0;
    private ProbabilityMap<EnemyType> enemyProbabilites = null;

    private int crystalCount = 0;
    private boolean objectiveComplete = false;
    private boolean levelComplete = false;

    public GameLevel(GameLevelSpec spec, Entity player) {
        this.size = spec.getSize();
        this.player = player;
        this.spec = spec;

        this.spawn(spec.createStartEntities());
        this.spawnPlayer();
    }

    public GameLevel(GameLevelSpec spec) {
        this(spec, EntityCreator.create(EntityType.PLAYER));
    }

    /**
     * Place an entity randomly on the map.
     * A PositionComponent will be added if the entity doesn't have one.
     * TODO avoid collisions
     **/
    private void spawn(Entity e) {
        PositionComponent pc;

        if (e.has(PositionComponent.class)) {
            pc = e.get(PositionComponent.class);
        } else {
            pc = new PositionComponent();
            e.add(pc);
        }

        pc.setPosition(new Vector2D(Math.random()*this.size, Math.random()*this.size));
        this.add(e);
    }

    private void spawn(List<Entity> entities) {
        for (Entity e : entities) {
            this.spawn(e);
        }
    }

    private void spawnPlayer() {
        Entity teleporter = EntityCreator.create(EntityType.TELEPORTER);
        teleporter.add(new TimerComponent(200));

        this.spawn(teleporter);
        this.player.get(PositionComponent.class).setPosition(
            teleporter.get(PositionComponent.class).getPosition()
        );
        this.add(this.player);
    }

    private void spawnEndTeleporter() {
        Entity teleporter = EntityCreator.create(EntityType.TELEPORTER);
        this.spawn(teleporter);
        this.objectiveComplete = true;
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

                if (cc1.collidesWith(cc2, this)) {
                    ent1.collideWith(ent2, this);
                    ent2.collideWith(ent1, this);
                }
            }
        }
    }

    public int getLevel() {
        return this.spec.level;
    }

    public boolean update() {
        this.updateWave();
        this.checkCollisions();
        this.updateEntities();
        return this.levelComplete;
    }

    public double getSize() {
        return this.size;
    }

    public void endLevel() {
        if (this.objectiveComplete) {
            this.levelComplete = true;
        }
    }

    public void add(Entity e) {
        if (e.has(PositionComponent.class)) {
            this.entities.add(e);
        } else {
            LOGGER.log(Level.WARNING, "can't add entity, no poscomp: " + e);
        }
    }
    
    public void addCrystal() {
        this.crystalCount++;
        if (this.crystalCount == this.spec.getCrystalCount()) {
            this.spawnEndTeleporter();
        }
    }

    public Entity getPlayer() {
        return this.player;
    }

    public LevelType getType() {
        return this.spec.levelType;
    }

    public List<Entity> getEntities() {
        return this.entities;
    }

    public Vector2D getObjectiveDirection(double interpolation) {
        Entity objective = null;

        if (this.objectiveComplete) {
            objective = this.closestOfType(EntityType.TELEPORTER, this.player);
        } else if (this.spec.levelType == LevelType.HARVEST) {
            objective = this.closestOfType(EntityType.COLLECTIBLE_CRYSTAL, this.player);
        }

        if (objective == null) {
            return null;
        } else {
            Vector2D objPos = objective.get(PositionComponent.class).futurePos(interpolation);
            Vector2D playerPos = this.player.get(PositionComponent.class).futurePos(interpolation);
            return Vector2D.subtract(
                this.wrapAround(playerPos, objPos),
                playerPos
            ).unit();
        }
    }
    
    public Entity closestOfType(EntityType type, Entity entity) {
        Vector2D entityPos = entity.get(PositionComponent.class).getPosition();
        Entity closestEntity = null;
        Vector2D closestPos = null;
        double minDistance = 0;
        for (Entity e : this.entities) {
            if (e.getType() == type) {
                Vector2D pos = e.get(PositionComponent.class).getPosition();
                Vector2D wrappedPos = this.wrapAround(entityPos, pos);
                double distance = Vector2D.distanceSq(entityPos, wrappedPos);
                if (closestPos == null || distance < minDistance) {
                    closestEntity = e;
                    closestPos = wrappedPos;
                    minDistance = distance;
                }
            }
        }

        return closestEntity;
    }

    /**
     * Wrap a coordinate around the game level if it's on the other side relative to p2.
     * <pre> {@code
     *
     * P   :  position of this component 
     * P'  :  position of point to possibly be wrapped around
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
     * @param p2 position to possibly wrap around
     * @return same position but possibly wrapped around.
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
