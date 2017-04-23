package se.liu.ida.noahe116.tddd78.swarm.game;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.logging.*;

import se.liu.ida.noahe116.tddd78.swarm.common.ProbabilityMap;
import se.liu.ida.noahe116.tddd78.swarm.game.*;

public class GameLevelSpec {
    private static final Logger LOGGER = Logger.getLogger(GameLevelSpec.class.getName());

    public static final Random RAND = new Random();

    private int size = 10000;

    private LevelType levelType;
    private int crystalCount = 0;

    private int minCollectibles = 0;
    private int maxCollectibles = 0;
    private ProbabilityMap<CollectibleType> collectibles = new ProbabilityMap<>();

    private int minAsteroids = 0;
    private int maxAsteroids = 0;

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

    public GameLevelSpec collectibleCountBetween(int min, int max) {
        this.minCollectibles = min;
        this.maxCollectibles = max;
        return this;
    }

    public GameLevelSpec asteroidCountBetween(int min, int max) {
        this.minAsteroids = min;
        this.maxAsteroids = max;
        return this;
    }

    public GameLevelSpec withWave(Wave wave) {
        if (this.waves.size() == 0) {
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

        int asteroids = this.minAsteroids +
            RAND.nextInt(this.maxAsteroids-this.minAsteroids + 1);
        int collectibles = this.minCollectibles +
            RAND.nextInt(this.maxCollectibles-this.minCollectibles + 1);

        for (int i = 0; i < asteroids; i++) {
            entities.add(EntityCreator.create(EntityType.ASTEROID));
        }

        for (int i = 0; i < collectibles; i++) {
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

    public long getEnemySpawnDelay(int wave) {
        return this.getWave(wave).enemySpawnDelay;
    }

    public long getStartTick(int wave) {
        return this.getWave(wave).startTick;
    }

    public int getMaxEnemies(int wave) {
        return this.getWave(wave).maxEnemies;
    }

    public int getSize() {
        return this.size;
    }
}
