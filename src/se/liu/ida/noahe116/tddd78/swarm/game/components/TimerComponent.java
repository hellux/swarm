package se.liu.ida.noahe116.tddd78.swarm.game.components;

import se.liu.ida.noahe116.tddd78.swarm.game.level.*;

public class TimerComponent extends LiveComponent {
    private int time;

    public TimerComponent(int time) {
        this.time = time;
    }

    @Override
    public void update(GameLevel level) {
        if (this.time == 0) {
            this.entity.kill();
        }
        this.time--;
    }
}
