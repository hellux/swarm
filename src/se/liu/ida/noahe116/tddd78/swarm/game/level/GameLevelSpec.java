package se.liu.ida.noahe116.tddd78.swarm.game.level;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

import se.liu.ida.noahe116.tddd78.swarm.common.ProbabilityMap;
import se.liu.ida.noahe116.tddd78.swarm.game.entities.EnemyType;
import se.liu.ida.noahe116.tddd78.swarm.game.entities.Entity;
import se.liu.ida.noahe116.tddd78.swarm.game.entities.EntityCreator;
import se.liu.ida.noahe116.tddd78.swarm.game.entities.EntityType;
import se.liu.ida.noahe116.tddd78.swarm.game.collectibles.CollectibleType;

/**
 * Specification for game level.
 */
public class GameLevelSpec {
    private int size;

    /**
     * type of level
     */
    public final LevelType levelType;
    /**
     * level number
     */
    public final int level;
    private int crystalCount = 0;
    private int lootTime;

    private int collectibleCount= 0;
    private ProbabilityMap<CollectibleType> collectibles = new ProbabilityMap<>();

    private int asteroidCount = 0;

    private List<Wave> waves = new ArrayList<>();

    public GameLevelSpec(LevelType levelType, int level) {
        this.levelType = levelType;
        this.level = level;
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

    public GameLevelSpec lootTime(int ticks) {
        this.lootTime = ticks;
        return this;
    }

    public GameLevelSpec withWave(Wave wave) {
        if (this.waves.isEmpty()) {
            if (wave.startTick != 0) {
                throw new IllegalArgumentException("first wave must start at tick 0");
            }
        }
        else if (this.waves.get(this.waves.size()-1).startTick >= wave.startTick) {
            throw new IllegalArgumentException(
                "wave must have start time later than previous wave: " + 
                this.waves.get(this.waves.size()-1).startTick + " >= " + wave.startTick);
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
    
    public int getLootTime() {
        return this.lootTime;
    }

    public int getEnemySpawnDelay(int wave) {
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

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (this.getClass() != o.getClass()) return false;
        GameLevelSpec gls = (GameLevelSpec) o;

        if (this.levelType != gls.levelType) return false;
        if (this.level != gls.level) return false;
        if (this.crystalCount != gls.crystalCount) return false;
        if (this.lootTime != gls.lootTime) return false;
        if (this.collectibleCount != gls.collectibleCount) return false;
        if (this.asteroidCount != gls.asteroidCount) return false;
        if (!this.collectibles.equals(gls.collectibles)) return false;
        if (!this.waves.equals(gls.waves)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 1;

        hash = hash*359 + this.levelType.hashCode();
        hash = hash*311 + this.level;
        hash = hash*37 + this.crystalCount;
        hash = hash*71 + this.lootTime;
        hash = hash*29 + this.collectibleCount;
        hash = hash*173 + this.asteroidCount;
        hash = hash*103 + this.collectibles.hashCode();
        hash = hash*23 + this.waves.hashCode();

        return hash;
    }
}
