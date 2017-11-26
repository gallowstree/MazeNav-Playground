package sample;

import io.vavr.collection.TreeSet;

import static sample.Direction.*;

public class MazeView {
    public Tile[][] realMaze;
    private Vec2 startingPoint;
    private MappingListener mappingListener;


    public MazeView(Tile[][] realMaze, Vec2 startingPoint, MappingListener mappingListener) {
        this.realMaze = realMaze;
        this.startingPoint = startingPoint;
        this.mappingListener = mappingListener;
    }

    protected Tile discover(Vec2 p, Direction newDir) {
        p = toGlobalCoordinates(p);
        Tile t = realMaze[p.x][p.y];
        t.visited = true;
        mappingListener.tileVisited(p, newDir);
        mappingListener.tileDiscovered(p, TreeSet.of(N,S,E,W).removeAll(t.canMoveTo));
        return t;
    }

    protected Vec2 toGlobalCoordinates(Vec2 v) {
        return v.plus(startingPoint);
    }
}
