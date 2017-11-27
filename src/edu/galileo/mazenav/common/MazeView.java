package edu.galileo.mazenav.common;

import edu.galileo.mazenav.MappingListener;
import edu.galileo.mazenav.StrategyRunner;
import io.vavr.collection.TreeSet;

public class MazeView {
    public Tile[][] realMaze;
    private Vec2 startingPoint;
    private MappingListener mappingListener;


    public MazeView(Tile[][] realMaze, Vec2 startingPoint, MappingListener mappingListener) {
        this.realMaze = realMaze;
        this.startingPoint = startingPoint;
        this.mappingListener = mappingListener;
    }

    public Tile discover(Vec2 p, Direction newDir) {
        p = toGlobalCoordinates(p);
        Tile t = realMaze[p.x][p.y];
        t.visited = true;
        mappingListener.tileVisited(p, newDir);
        mappingListener.tileDiscovered(p, TreeSet.of(Direction.N, Direction.S, Direction.E, Direction.W).removeAll(t.canMoveTo));
        return t;
    }

    public Vec2 toGlobalCoordinates(Vec2 v) {
        return v.plus(startingPoint);
    }

    public void setMappingListener(StrategyRunner mappingListener) {
        this.mappingListener = mappingListener;
    }
}
