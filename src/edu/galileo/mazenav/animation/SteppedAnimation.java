package edu.galileo.mazenav.animation;

import java.util.Optional;

public interface SteppedAnimation<T> {
    T initial();

    Optional<T> step(T curr);

    default void runAtOwnSpeed() {}

    default void draw(T state) {}
}
