package se.liu.ida.noahe116.tddd78.swarm.game;

public class Game {
    private int score;
    private int maxLevel;
   
    private int currentLevel;

    private Game(int level) {
        this.score = 0;

        this.maxLevel = level;
    }

    public Game() {
        this(1);
    }

    public Game(String savedSession) {
        this(1);
    }
}
