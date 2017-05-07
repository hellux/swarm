package se.liu.ida.noahe116.tddd78.swarm.test;

import se.liu.ida.noahe116.tddd78.swarm.game.level.*;

/**
 * Test the GameLevelCreator class.
 **/
public final class TestGameLevelCreator implements Test {
    public static final String[] SEEDS =
        new String[] {"tnoeahutnh", "noah", "/()&/(&(()/(()/&"};

    public void run() {
        testConsistency();
    }

    /**
     * Test if levels created with the same seed 
     * always produce the same level spec.
     **/
    private void testConsistency() {
        for (String seed : SEEDS) {
            GameLevelCreator glc1 = new GameLevelCreator(seed);
            GameLevelCreator glc2 = new GameLevelCreator(seed);

            GameLevelSpec gls1 = glc1.getSpec(234);
            glc2.getSpec(1);
            glc2.getSpec(2);
            GameLevelSpec gls2 = glc2.getSpec(234);

            assert gls1.equals(gls2);
            assert !gls1.equals(new GameLevelCreator("NOEHUNTHOEU").getSpec(124));
        }
    }
}
