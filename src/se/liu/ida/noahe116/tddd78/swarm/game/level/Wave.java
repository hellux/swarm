package se.liu.ida.noahe116.tddd78.swarm.game.level;

import se.liu.ida.noahe116.tddd78.swarm.common.ProbabilityMap;
import se.liu.ida.noahe116.tddd78.swarm.game.entities.EnemyType;

public class Wave {
    public final long startTick;

    public int maxEnemies = -1;
    public long enemySpawnDelay = -1;
    public ProbabilityMap<EnemyType> enemies = new ProbabilityMap<>();

    public Wave(int startTick) {
	this.startTick = startTick;
    }

    public Wave() {
    	this(0);
    }

    public Wave withSpawnDelay(long ticks) {
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
