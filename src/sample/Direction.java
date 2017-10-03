package sample;

import java.util.HashMap;

public enum Direction {

    N(new Vec2(-1,0)),
    S(new Vec2(1,0)),
    E(new Vec2(0,1)),
    W(new Vec2(0,-1));

    Direction(Vec2 displacement) {
        this.displacement = displacement;
    }

    public final Vec2 displacement;

    public Vec2 displace(Vec2 initial) {
        return initial.plus(displacement);
    }

    public Direction invert() {
        return inverse.get(this);
    }

    private static HashMap<Direction, Direction> inverse = new HashMap<>();
    static {
        inverse.put(N, S);
        inverse.put(S, N);
        inverse.put(E, W);
        inverse.put(W, E);
    }
}
