package se.liu.ida.noahe116.tddd78.swarm.game;

import se.liu.ida.noahe116.tddd78.swarm.game.level.*;

/**
 * Keep track of game session and create game levels.
 **/
public class Game {
    private int maxLevel;
    private String name;
    private GameLevelCreator gameLevelCreator;
   
    private int currentLevel = 0;

    public Game(int level, String name) {
        if (level < 1) throw new IllegalArgumentException("Invalid level!" + level);
        this.maxLevel = level;
        this.name = name;
        this.gameLevelCreator = new GameLevelCreator(name);
    }

    public boolean validLevel(int level) {
        return 0 < level && level <= this.maxLevel;
    }

    public GameLevel getLevel() {
        return this.gameLevelCreator.createLevel(this.currentLevel);
    }

    public void setCurrentLevel(int level) {
        if (this.validLevel(level)) {
            this.currentLevel = level;
        } else {
            throw new IllegalArgumentException("Invalid level: " + level);
        }
    }

    /**
     * Get a GameLevel one level higher with the same player.
     * @param gameLevel current game level
     **/
    public GameLevel getNextLevel(GameLevel gameLevel) {
        int level = gameLevel.getLevel();
        if (level == this.maxLevel) {
            this.maxLevel++;
        }
        this.currentLevel = level+1;
        return this.gameLevelCreator.createLevel(this.currentLevel, gameLevel.getPlayer());
    }

    public int getMaxLevel() {
        return this.maxLevel;
    }

    public String getName() {
        return this.name;
    }
}
