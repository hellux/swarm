package se.liu.ida.noahe116.tddd78.swarm.test;

import se.liu.ida.noahe116.tddd78.swarm.common.ProbabilityMap;
import se.liu.ida.noahe116.tddd78.swarm.game.level.*;
import se.liu.ida.noahe116.tddd78.swarm.game.collectibles.CollectibleType;
import se.liu.ida.noahe116.tddd78.swarm.game.entities.EnemyType;

/**
 * Test the GameLevelSpec class.
 **/
 public final class TestGameLevelSpec implements Test {
    public void run() {
        testEquals();
    }

    private void testEquals() {
        GameLevelSpec gls1 = new GameLevelSpec(LevelType.HARVEST, 5)
            .ofSize(1489)
            .collectibleCount(30)
            .crystalCount(3)
            .withCollectibles(new ProbabilityMap<CollectibleType>()
                .put(CollectibleType.CRYSTAL, 4)
            )
            .withWave(new Wave()
                .withSpawnDelay(19)
                .withEnemies(new ProbabilityMap<EnemyType>()
                    .put(EnemyType.CLAG_BOT, 23)
                )
                .maxEnemyCount(39)
            );

        GameLevelSpec gls2 = new GameLevelSpec(LevelType.HARVEST, 5)
            .ofSize(1489)
            .collectibleCount(30)
            .crystalCount(3)
            .withCollectibles(new ProbabilityMap<CollectibleType>()
                .put(CollectibleType.CRYSTAL, 4)
            )
            .withWave(new Wave()
                .withSpawnDelay(19)
                .withEnemies(new ProbabilityMap<EnemyType>()
                    .put(EnemyType.CLAG_BOT, 23)
                )
                .maxEnemyCount(39)
            );

        GameLevelSpec gls3 = new GameLevelSpec(LevelType.HARVEST, 5)
            .ofSize(1489)
            .collectibleCount(30)
            .crystalCount(3)
            .withCollectibles(new ProbabilityMap<CollectibleType>()
                .put(CollectibleType.CRYSTAL, 4)
            )
            .lootTime(3)
            .withWave(new Wave()
                .withSpawnDelay(19)
                .withEnemies(new ProbabilityMap<EnemyType>()
                    .put(EnemyType.CLAG_BOT, 23)
                )
                .maxEnemyCount(39)
            );

        assert gls1.equals(gls2);
        assert !gls1.equals(gls3);
    }
 }
