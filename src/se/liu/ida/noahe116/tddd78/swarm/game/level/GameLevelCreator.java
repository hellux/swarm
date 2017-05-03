package se.liu.ida.noahe116.tddd78.swarm.game.level;

import java.util.function.Function;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

import se.liu.ida.noahe116.tddd78.swarm.common.ProbabilityMap;
import se.liu.ida.noahe116.tddd78.swarm.game.entities.EnemyType;
import se.liu.ida.noahe116.tddd78.swarm.game.collectibles.CollectibleType;
import se.liu.ida.noahe116.tddd78.swarm.game.entities.*;

/**
 * Creates levels with a specific seed.
 * All levels before a certain one must be generated before in the exact same order 
 * to make sure that levels generated with identical seeds have identical specs.
 **/
public final class GameLevelCreator {
    private final ProbabilityMap<Function<Integer, GameLevelSpec>> GENERATORS =
        new ProbabilityMap<>(); 

    private final Random rand;
    private final List<GameLevelSpec> specs;

    {
        GENERATORS.put(this::generateHarvestSpec, 10);
        GENERATORS.put(this::generateLootSpec, 1);
    }
    
    public GameLevelCreator(long seed) {
        this.rand = new Random(seed);
        this.specs = new ArrayList<>();
    }

    public GameLevelCreator(String seed) {
        this((long) seed.hashCode());
    }

    private void generateSpecs(int level) {
        for (int i = specs.size(); i < level; i++) {
            this.specs.add(this.GENERATORS.get(this.rand).apply(level));
        }
    }

    /**
     * Generate non-uniform random doubles with normal distribution.
     **/
    private double normRand(double mean, double standardDeviation) {
        return mean + standardDeviation*this.rand.nextGaussian();
    }
    
    /**
     * Generate non-uniform random doubles with half-normal distribution.
     **/
    private double halfNormRand(double mean, double standardDeviation) {
        return mean + standardDeviation*Math.abs(this.rand.nextGaussian());
    }

    private GameLevelSpec generateHarvestSpec(int level) {
        GameLevelSpec spec = new GameLevelSpec(LevelType.HARVEST, level);

        int waveCount = (int) this.halfNormRand(1 + Math.log(level), 0.2);
        for (int i = 0; i < waveCount; i++) {
            spec.withWave(new Wave((int) (i*halfNormRand(Math.pow(i, 2), i)))
                .maxEnemyCount((int) (Math.log(level) + normRand(10, 3)))
                .withEnemies(new ProbabilityMap<EnemyType>()
                    .put(EnemyType.CLAG_BOT, 1))
                .withSpawnDelay((long) (5.0/level*Math.pow(Math.sin(Math.PI*i/waveCount), 2))));
        }

        return spec
            .ofSize(5000 + (int)((Math.log(level)+1)*3000))
            .asteroidCount(5 + (int) normRand(level, level/3.0))
            .crystalCount(5 + (int) halfNormRand(level, level))
            .collectibleCount(5 + (int) normRand(level, level/3.0))
            .withCollectibles(new ProbabilityMap<CollectibleType>()
                .put(CollectibleType.SHIELD, 5)
                .put(CollectibleType.SHIP, 0.5)
                .put(CollectibleType.RED_LASER, halfNormRand(level/10.0, 3))
                .put(CollectibleType.SPREAD, halfNormRand(level/20.0, 2))
                .put(CollectibleType.QUAD, halfNormRand(Math.log(level)/30.0, 1)));
    }

    private GameLevelSpec generateLootSpec(int level) {
        GameLevelSpec spec = new GameLevelSpec(LevelType.LOOT, level);
        return spec
            .ofSize((int)((Math.log(level)+1)*4000))
            .withWave(new Wave()
                .maxEnemyCount(0)
            )
            .lootTime((int) normRand(1000, 100))
            .asteroidCount(5 + (int) normRand(level, level/3.0))
            .collectibleCount(10 + (int) normRand(level, level/3.0))
            .withCollectibles(new ProbabilityMap<CollectibleType>()
                .put(CollectibleType.SHIELD, 1)
                .put(CollectibleType.SHIP, 0.3)
                .put(CollectibleType.RED_LASER, halfNormRand(level/10.0, 3))
                .put(CollectibleType.SPREAD, halfNormRand(level/20.0, 2))
                .put(CollectibleType.QUAD, halfNormRand(level/30.0, 1)));
    }

    private GameLevelSpec getSpec(int level) {
        // Makes sure RNG sequence is equal for equal seeds
        // Specs are always generated from level 1 in order
        if (this.specs.size() < level) {
            this.generateSpecs(level);
        }

        return this.specs.get(level-1);
    }

    /**
     * Returns a specific level generated with the seed of the instance.
     * A specific seed and level number will always produce a level 
     * with identical properties to other levels with the same level
     * number and seed.
     * @param level number of level
     * @return generated level
     **/
    public GameLevel createLevel(int level) {
        return new GameLevel(this.getSpec(level));
    }

    public GameLevel createLevel(int level, Entity player) {
        return new GameLevel(this.getSpec(level), player);
    }
}
