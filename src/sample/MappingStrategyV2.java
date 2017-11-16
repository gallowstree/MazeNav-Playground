package sample;

import io.vavr.collection.List;
import io.vavr.collection.SortedSet;
import io.vavr.collection.TreeSet;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static sample.Direction.*;

public class MappingStrategyV2  {

    private Tile[][] realMaze;
    private Vec2 startingPoint;
    private MappingListener mappingListener;

    Set<Vec2> visited = new HashSet<>();

    public MappingStrategyV2(MappingListener mappingListener) {
        this.mappingListener = mappingListener;
    }

    public State setup(Tile[][] maze, Vec2 start, Direction initialDirection) {
        realMaze = maze;
        startingPoint = start;

        Vec2 initialPos = new Vec2(0,0);

        Tile t = discover(initialPos);
        List<Vec2> pending = List.empty();

        pending = pushAll(t.canMoveTo, initialDirection, initialPos, pending);

        return new State(initialPos, initialDirection, pending);
    }

    private List<Vec2> pushAll(SortedSet<Direction> canMoveTo, Direction facing, Vec2 pos, List<Vec2> pending) {


        List<Vec2> finalPending = pending;
        pending = pending.pushAll(
                canMoveTo.toJavaSet().stream().filter(d -> !d.equals(facing))
                        .map(direction -> direction.displace(pos))
                        .filter(p -> !visited.contains(p) && !finalPending.contains(p))
                        .collect(Collectors.toList()));

        if (canMoveTo.contains(facing)) {
            pending = pending.push(facing.displace(pos));
        }

        return pending;
    }

    public State step(State s) {
         if (s.pendingStack.isEmpty())
            return null;

        Vec2 newPos = s.pendingStack.head();
        Direction newDir = newPos.direction;
        List<Vec2> newStack = s.pendingStack.pop();

        visited.add(newPos);
        mappingListener.tileVisited(toGlobalCoordinates(newPos), newDir);

        newStack = pushAll(discover(newPos).excluding(newDir.invert()).canMoveTo, newDir, newPos, newStack);

        return new State(newPos, newDir, newStack);
    }

    protected Tile discover(Vec2 p) {
        p = toGlobalCoordinates(p);
        Tile t = realMaze[p.x][p.y];
        t.visited = true;
        mappingListener.tileDiscovered(p, TreeSet.of(N,S,E,W).removeAll(t.canMoveTo));
        return t;
    }

    protected Vec2 toGlobalCoordinates(Vec2 v) {
        return v.plus(startingPoint);
    }

    static class State {
        List<Vec2> pendingStack;
        Vec2 position;
        Direction facing;
        public State(Vec2 position, Direction facing, List<Vec2> pendingStack) {
            this.pendingStack = pendingStack;
            this.position = position;
            this.facing = facing;
        }
    }
}
