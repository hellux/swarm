package se.liu.ida.noahe116.tddd78.swarm.game.level;

import se.liu.ida.noahe116.tddd78.swarm.common.ProbabilityMap;
import se.liu.ida.noahe116.tddd78.swarm.game.entities.EnemyType;

/**
 * Specify properties of a wave of enemies.
 *
 * Can be initialized like:
 * <pre> {@code
 * 	new Wave()
 * 		.withSpawnDelay(d)
 * 		.withEnemies(e)
 * 		.maxEnemyCount(m);
 * } </pre>
 * This is more to make it more readable compared to using a constructor.
 */
public class Wave {
    /**
     * tick when wave should start
     */
    public final long startTick;

    /**
     * maximum amount of enemies
     */
    public int maxEnemies;

    /**
     * delay for respawning a new enemy
     */
    public int enemySpawnDelay = 0;
    /**
     * probabilities for different types of enemies to spawn
     */
    public ProbabilityMap<EnemyType> enemies = new ProbabilityMap<>();

    public Wave(int startTick) {
	this.startTick = startTick;
    }

    public Wave() {
    	this(0);
    }

    public Wave withSpawnDelay(int ticks) {
	this.enemySpawnDelay = ticks;
	return this;
    }

    public Wave withEnemies(ProbabilityMap<EnemyType> enemies) {
	    this.enemies.put(enemies);
	    return this;
    }

    public Wave maxEnemyCount(int max) {
	this.maxEnemies = max;
	return this;
    }
}
