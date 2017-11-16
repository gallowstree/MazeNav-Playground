package sample;

import io.vavr.collection.TreeSet;

import static sample.Direction.*;

public class MazeView {
    private Tile[][] realMaze;
    private Vec2 startingPoint;


    protected Tile discover(Vec2 p) {
        p = toGlobalCoordinates(p);
        Tile t = realMaze[p.x][p.y];
        t.visited = true;
        return t;
    }



    protected Vec2 toGlobalCoordinates(Vec2 v) {
        return v.plus(startingPoint);
    }
}
