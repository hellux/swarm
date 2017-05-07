package se.liu.ida.noahe116.tddd78.swarm.test;

import se.liu.ida.noahe116.tddd78.swarm.common.ProbabilityMap;

public final class TestProbabilityMap implements Test {
    public void run() {
        testEquals();
    }

    private void testEquals() {
        ProbabilityMap<Integer> pm1 = new ProbabilityMap<Integer>()
            .put(7, 3)
            .put(3, 2)
            .put(1, 2);
            
        ProbabilityMap<Integer> pm2 = new ProbabilityMap<Integer>()
            .put(7, 3)
            .put(3, 2)
            .put(1, 2);

        ProbabilityMap<Integer> pm3 = new ProbabilityMap<Integer>()
            .put(7, 3)
            .put(3, 1)
            .put(1, 2);

        assert pm1.equals(pm2);
        assert !pm1.equals(pm3);
    }
}
