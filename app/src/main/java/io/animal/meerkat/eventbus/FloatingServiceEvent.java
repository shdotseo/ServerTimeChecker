package io.animal.meerkat.eventbus;

public class FloatingServiceEvent {

    private FloatingServiceStatus status;

    public FloatingServiceEvent(FloatingServiceStatus status) {
        this.status = status;
    }

    public FloatingServiceStatus getStatus() {
        return status;
    }
}
