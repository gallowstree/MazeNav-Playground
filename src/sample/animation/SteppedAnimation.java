package sample.animation;

import java.util.Optional;

public interface SteppedAnimation<T> {
    T initial();

    Optional<T> step(T curr);

    void runAtOwnSpeed();
}
