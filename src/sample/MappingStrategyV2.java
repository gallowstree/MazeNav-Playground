package sample;

import io.vavr.collection.List;
import io.vavr.collection.SortedSet;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


public class MappingStrategyV2  {

    private MappingListener mappingListener;
    private Set<Vec2> visited = new HashSet<>();
    private MazeView maze;
    private State initialState;


    public void setup(MazeView maze, Vec2 start, Direction initialDirection) {
        this.maze = maze;

        Vec2 initialPos = new Vec2(0,0);
        Tile t = maze.discover(initialPos, initialDirection);
        List<Vec2> pending = List.empty();

        pending = pushInOrder(t.canMoveTo, initialDirection, initialPos, pending);

        initialState = new State(initialPos, initialDirection, pending);
    }

    public State initial() {
        return initialState;
    }

    public Optional<State> step(State s) {
         if (s.pendingStack.isEmpty())
            return Optional.empty();

        Vec2 newPos = s.pendingStack.head();
        Direction newDir = newPos.direction;
        List<Vec2> newStack = s.pendingStack.pop();

        visited.add(newPos);

        newStack = newStack.removeAll(newPos);
        newStack = pushInOrder(maze.discover(newPos, newDir).excluding(newDir.invert()).canMoveTo, newDir, newPos, newStack);

        return Optional.of(new State(newPos, newDir, newStack));
    }

    private List<Vec2> pushInOrder(SortedSet<Direction> canMoveTo, Direction facing, Vec2 pos, List<Vec2> pending) {

        pending = pending.pushAll(
                canMoveTo.toJavaSet().stream().filter(d -> !d.equals(facing))
                        .map(direction -> direction.displace(pos))
                        .filter(p -> !visited.contains(p))
                        .collect(Collectors.toList()));

        Vec2 front = facing.displace(pos);
        if (canMoveTo.contains(facing) && !pending.contains(front) && !visited.contains(front)) {
            pending = pending.push(front);
        }

        return pending;
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
