package se.liu.ida.noahe116.tddd78.swarm.game.level;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.*;

import se.liu.ida.noahe116.tddd78.swarm.common.ProbabilityMap;
import se.liu.ida.noahe116.tddd78.swarm.game.entities.EnemyType;
import se.liu.ida.noahe116.tddd78.swarm.game.entities.Entity;
import se.liu.ida.noahe116.tddd78.swarm.game.entities.EntityCreator;
import se.liu.ida.noahe116.tddd78.swarm.game.entities.EntityType;
import se.liu.ida.noahe116.tddd78.swarm.game.collectibles.CollectibleType;

public class GameLevelSpec {
    private static final Logger LOGGER = Logger.getLogger(GameLevelSpec.class.getName());

    private int size = 15000;

    public final LevelType levelType;
    private int crystalCount = 0;

    private int collectibleCount= 0;
    private ProbabilityMap<CollectibleType> collectibles = new ProbabilityMap<>();

    private int asteroidCount = 0;

    private List<Wave> waves = new ArrayList<>();

    public GameLevelSpec(LevelType levelType) {
        this.levelType = levelType;
    }

    public GameLevelSpec ofSize(int size) {
        this.size = size;
        return this;
    }

    public GameLevelSpec withCollectibles(ProbabilityMap<CollectibleType> collectibles) {
        this.collectibles = collectibles;
        return this;
    }

    public GameLevelSpec collectibleCount(int count) {
        this.collectibleCount = count;
        return this;
    }

    public GameLevelSpec asteroidCount(int count) {
        this.asteroidCount = count;
        return this;
    }

    public GameLevelSpec crystalCount(int count) {
        this.crystalCount = count;
        return this;
    }

    public GameLevelSpec withWave(Wave wave) {
        if (this.waves.isEmpty()) {
            if (wave.startTick != 0) {
                LOGGER.log(Level.WARNING,
                           "first wave must start at 0 ticks not " + wave.startTick);
                return null;
            }
        }
        else if (this.waves.get(this.waves.size()-1).startTick >= wave.startTick) {
            LOGGER.log(Level.WARNING, "wave must have start time later than previous wave");
            return null;
        }

        this.waves.add(wave);

        return this;
    }

    private Wave getWave(int wave) {
        if (1 <= wave && wave <= this.waves.size()) {
            return this.waves.get(wave-1);
        } else {
            throw new IllegalArgumentException("Invalid wave: " + wave);
        }
    }

    public List<Entity> createStartEntities() {
        List<Entity> entities = new LinkedList<>();

        for (int i = 0; i < asteroidCount; i++) {
            entities.add(EntityCreator.create(EntityType.ASTEROID));
        }
        for (int i = 0; i < collectibleCount; i++) {
            entities.add(EntityCreator.create(this.collectibles.get()));
        } 

        if (this.levelType == LevelType.HARVEST) {
            for (int i = 0; i < crystalCount; i++) {
                entities.add(EntityCreator.create(CollectibleType.CRYSTAL));
            }
        }

        return entities;
    }

    /**
     * Return a map of probabilites of different enemyTypes at a
     * certain wave.
     * @param wave number of wave with probabilities
     * @return probability map
     **/
    public ProbabilityMap<EnemyType> getEnemyProbabilites(int wave) {
        ProbabilityMap<EnemyType> enemies = new ProbabilityMap<>();
        for (int i = 1; i <= wave; i++) {
            enemies.put(this.getWave(wave).enemies);
        }
        return enemies;
    }

    public int getCrystalCount() {
        return this.crystalCount;
    }

    public int getWaveCount() {
        return this.waves.size();
    }

    public long getEnemySpawnDelay(int wave) {
        return this.getWave(wave).enemySpawnDelay;
    }

    public long getNextWaveTick(int wave) {
        return this.getWave(wave+1).startTick;
    }

    public int getMaxEnemies(int wave) {
        return this.getWave(wave).maxEnemies;
    }

    public int getSize() {
        return this.size;
    }
}
