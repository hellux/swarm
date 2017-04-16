package se.liu.ida.noahe116.tddd78.swarm.game.components;

public class TimerComponent extends LiveComponent {
    int time;

    public TimerComponent(int time) {
        this.time = time;
    }

    public void update() {
        if (this.time-- == 0) {
            this.entity.kill();
        }
    }
}
