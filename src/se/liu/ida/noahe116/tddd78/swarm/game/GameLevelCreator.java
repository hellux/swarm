package se.liu.ida.noahe116.tddd78.swarm.game;  

import java.util.function.Function;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

import se.liu.ida.noahe116.tddd78.swarm.common.ProbabilityMap;

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
        GENERATORS.put(this::generateLootSpec, 2);
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

    private GameLevelSpec generateHarvestSpec(int level) {
        return null;
    }

    private GameLevelSpec generateEliminateSpec(int level) {
        return null;
    }

    private GameLevelSpec generateLootSpec(int level) {
        return null;
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
        if (this.specs.size() < level) {
            this.generateSpecs(level);
        }

        return new GameLevel(this.specs.get(level-1));
    }
}
