package io.animal.meerkat.eventbus;

public class TimerEvent {

    private long time;

    public TimerEvent(long time) {
        this.time = time;
    }

    public long getTime() {
        return time;
    }
}
