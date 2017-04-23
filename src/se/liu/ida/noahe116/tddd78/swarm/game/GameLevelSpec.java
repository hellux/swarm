package se.liu.ida.noahe116.tddd78.swarm.game;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import se.liu.ida.noahe116.tddd78.swarm.common.ProbabilityMap;
import se.liu.ida.noahe116.tddd78.swarm.game.*;

public class GameLevelSpec {
    public Random RAND = new Random();

    private int size = 10000;

    private LevelType levelType = null; 
    private int crystalCount = -1;

    private int minCollectibles = 0;
    private int maxCollectibles = 0;
    private ProbabilityMap<CollectibleType> collectibles = new ProbabilityMap<>();

    private int minAsteroids = 0;
    private int maxAsteroids = 0;

    private List<Wave> waves = new ArrayList<>();

    public class Wave {
        public final long startTick;

        public int maxEnemies = -1;
        public long enemySpawnDelay = -1;
        public ProbabilityMap<EnemyType> enemies = null;

        public Wave(int startTick) {
            this.startTick = startTick;
        }

        public void withSpawnRate(long ticks) {
            this.enemySpawnDelay = ticks;
        }

        public void withEnemies(ProbabilityMap<EnemyType> enemies) {
            this.enemies = enemies;
        }

        public void maxEnemyCount(int max) {
            this.maxEnemies = max;
        }
    }

    private Wave getWave(int wave) {
        if (1 <= wave && wave <= this.waves.size()) {
            return this.waves.get(wave-1);
        } else {
            throw new IllegalArgumentException("Invalid wave: " + wave);
        }
    }

    public void ofSize(int size) {
        this.size = size;
    }

    public void collectibleCountBetween(int min, int max) {
        this.minCollectibles = min;
        this.maxCollectibles = max;
    }

    public void asteroidCountBetween(int min, int max) {
        this.minAsteroids = min;
        this.maxAsteroids = max;
    }

    public List<Entity> getStartEntities() {
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
                //
            }
        }

        return entities;
    }

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
