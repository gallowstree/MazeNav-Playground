package sample;

public final class Vec2 {
    public final int x, y;

    public Vec2(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vec2 plus(Vec2 displacement) {
        return new Vec2(x + displacement.x, y + displacement.y);
    }

    @Override
    public String toString() {
        return "(" +
                "x=" + x +
                ", y=" + y +
                ')';
    }
}
