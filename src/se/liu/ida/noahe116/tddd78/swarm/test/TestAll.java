package se.liu.ida.noahe116.tddd78.swarm.test;

/**
 * Run all tests.
 **/
public final class TestAll {
    private static final Test[] TESTS = new Test[]
    {
        new TestProbabilityMap(),
        new TestGameLevelSpec(),
        new TestGameLevelCreator()
    };

    private TestAll() {};

    public static void main(String[] args) {
        for (Test test : TESTS) {
            test.run();
        }
    }
}
