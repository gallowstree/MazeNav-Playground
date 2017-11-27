package edu.galileo.mazenav.common;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum Direction {

    N(new Vec2(-1,0)),
    S(new Vec2(1,0)),
    E(new Vec2(0,1)),
    W(new Vec2(0,-1));

    private static HashMap<Direction, Direction> inverse = new HashMap<>();
    private static final Map<String, Direction> fromStr = new HashMap<>();

    Direction(Vec2 displacement) {
        this.displacement = displacement;
    }

    public final Vec2 displacement;

    public Vec2 displace(Vec2 initial) {
        return initial.plus(displacement).withDirection(this);
    }

    public Direction invert() {
        return inverse.get(this);
    }

    public static Optional<Direction> fromString(String val) {
        return Optional.ofNullable(fromStr.get(val));
    }

    static {
        inverse.put(N, S);
        inverse.put(S, N);
        inverse.put(E, W);
        inverse.put(W, E);

        fromStr.put(Character.toString('0'), W);
        fromStr.put(Character.toString('1'), N);
        fromStr.put(Character.toString('2'), E);
        fromStr.put(Character.toString('3'), S);
    }
}
