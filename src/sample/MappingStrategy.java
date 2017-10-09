package sample;

import io.vavr.collection.List;
import io.vavr.collection.SortedSet;

import static java.text.MessageFormat.format;
import static sample.MappingStrategy.GlobalState.gs;
import static sample.Utils.debug;
import static sample.Utils.str;
import static sample.Utils.tile;

public class MappingStrategy {
    private Tile[][] realMaze;
    private Vec2 startingPoint;
    private MappingListener mappingListener;

    public MappingStrategy(MappingListener mappingListener) {
        this.mappingListener = mappingListener;
    }

    public void run(Tile[][] maze, Vec2 start, Direction initialDirection) {
        realMaze = maze;
        startingPoint = start;

        Vec2 initialPos = new Vec2(0,0);
        Tile initialTile = discover(initialPos);
        State initialState = new State(initialPos, initialTile, initialDirection);

        iterate(new GlobalState(initialState, List.empty()));
    }

    private void iterate(GlobalState globalState) {
        debug(globalState.state, globalState.breadCrumbs);
        State s = globalState.state;
        List<BreadCrumb> bc = globalState.breadCrumbs;

        if (s.succesors.canMoveTo.isEmpty() && bc.isEmpty()) {
            return;
        } else if (s.succesors.canMoveTo.isEmpty()) {
            iterate(transition(s.position, s.facing.invert(), bc));
        } else {
            Direction d = chooseDirection(s.succesors.canMoveTo, s.facing);

            if (!bc.isEmpty() && s.facing.displace(bc.head().position).equals(s.position)) {
                bc = bc.push(new BreadCrumb(tile(s.facing.invert()), s.position));
            }

            if (s.succesors.canMoveTo.size() > 1) {
                BreadCrumb newBreadCrumb = new BreadCrumb(s.succesors.excluding(d), s.position);
                bc = bc.push(newBreadCrumb);
            }

            iterate(transition(s.position, d, bc));
        }
    }

    private GlobalState transition(Vec2 position, Direction chosenDirection, List<BreadCrumb> bc) {
        Vec2 newPosition = chosenDirection.displace(position);
        Tile tile;

        if (!bc.isEmpty() && bc.head().position.equals(newPosition)) {
            tile = bc.head().successors;
            bc = bc.pop();
        } else {
            tile =  discover(newPosition).excluding(chosenDirection.invert());
        }
        return gs(new State(newPosition, tile, chosenDirection), bc);
    }

    private Direction chooseDirection(SortedSet<Direction> canMoveTo, Direction facing) {
        if (canMoveTo.isEmpty()) {
            return facing.invert();
        }
        return canMoveTo.contains(facing) ? facing : canMoveTo.get();
    }

    private Tile discover(Vec2 p) {
        p = toGlobalCoordinates(p);
        realMaze[p.x][p.y].visited = true;
        return realMaze[p.x][p.y];
    }

    private Vec2 toGlobalCoordinates(Vec2 v) {
        return v.plus(startingPoint);
    }

    static class State {

        final Vec2 position;
        final Tile succesors;
        final Direction facing;
        State(Vec2 position, Tile succesors, Direction facing) {
            this.position = position;
            this.succesors = succesors;
            this.facing = facing;
        }

        @Override
        public String toString() {
            return format("<{0}, {1}, {2}>", str(position), str(succesors.canMoveTo), facing);
        }

    }

    static class BreadCrumb {
        final Vec2 position;
        final Tile successors;

        BreadCrumb(Tile successors) {
            this(successors, null);
        }

        BreadCrumb(Tile successors, Vec2 position) {
            this.position = position;
            this.successors = successors;
        }

        @Override
        public String toString() {
            return "<" + (position == null ? "" : position + ", " )+ successors.canMoveTo + ">"  ;
        }
    }

    static class GlobalState {
        final State state;
        final List<BreadCrumb> breadCrumbs;

        GlobalState(State state, List<BreadCrumb> breadCrumbs) {
            this.state = state;
            this.breadCrumbs = breadCrumbs;
        }

        static GlobalState gs(State state, List<BreadCrumb> breadCrumbs) {
            return new GlobalState(state, breadCrumbs);
        }
    }
}

