package se.liu.ida.noahe116.tddd78.swarm.game.level;

import java.util.function.Function;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

import se.liu.ida.noahe116.tddd78.swarm.common.ProbabilityMap;
import se.liu.ida.noahe116.tddd78.swarm.game.entities.EnemyType;
import se.liu.ida.noahe116.tddd78.swarm.game.collectibles.CollectibleType;

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
        GENERATORS.put(this::generateHarvestSpec, 20);
        GENERATORS.put(this::generateEliminateSpec, 1);
        GENERATORS.put(this::generateLootSpec, 2000);
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
            this.specs.add(this.GENERATORS.get().apply(level));
        }
    }

    private int nonUniformRandInt(int average, int standardDeviation) {
        return average + (int) (standardDeviation*this.rand.nextGaussian());
    }

    private GameLevelSpec generateHarvestSpec(int level) {
        GameLevelSpec spec = new GameLevelSpec(LevelType.HARVEST);

        return spec;
    }

    private GameLevelSpec generateEliminateSpec(int level) {
        GameLevelSpec spec = new GameLevelSpec(LevelType.ELIMINATE);

        return spec;
    }

    private GameLevelSpec generateLootSpec(int level) {
        return new GameLevelSpec(LevelType.HARVEST)
            .withWave(new Wave()
                .maxEnemyCount(20)
                .withEnemies(new ProbabilityMap<EnemyType>()
                    .put(EnemyType.CLAG_BOT, 1))
                .withSpawnDelay(10))
            .asteroidCount(15)
            .crystalCount(10)
            .collectibleCount(25)
            .withCollectibles(new ProbabilityMap<CollectibleType>()
                .put(CollectibleType.SHIELD, 2)
                .put(CollectibleType.RED_LASER, 1)
                .put(CollectibleType.SPREAD, 1)
                .put(CollectibleType.QUAD, 1));
    }

    /**
     * Returns a specific level generated with the seed of the instance.
     * A specific seed and level number will always produce a level 
     * with identical properties to other levels with the same level
     * number and seed.
     * @param level number of level
     * @return generated level
     **/
    public GameLevel getLevel(int level) {
        // Makes sure RNG sequence is equal for equal seeds
        // Specs are always generated from level 1 in order
        if (this.specs.size() < level) {
            this.generateSpecs(level);
        }

        return new GameLevel(this.specs.get(level-1));
    }
}
