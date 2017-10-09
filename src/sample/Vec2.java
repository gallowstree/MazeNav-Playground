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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Vec2 vec2 = (Vec2) o;
        return vec2.x == x && vec2.y == y;
    }
}
