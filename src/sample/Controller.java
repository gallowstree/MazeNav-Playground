package sample;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.SortedSet;
import io.vavr.collection.TreeSet;

import java.text.MessageFormat;
import java.util.Optional;

import static sample.Direction.*;

public class Controller {

    Tile[][] realMaze = {
            new Tile[] {tile(E),    tile(S,W),    tile(S,E),      tile(E,W), tile(S,W)},
            new Tile[] {tile(S,E),  tile(N,W),    tile(N,S),      tile(S),   tile(N)},
            new Tile[] {tile(N,E),  tile(E,W),    tile(N,S,W),    tile(N,E), tile(S, W)},
            new Tile[] {tile(E),    tile(E,W),    tile(N,E,W),    tile(E,W), tile(N, W)},
    };
    private final Vec2 startingPoint = new Vec2(2,2);

    private Tile tile(Direction... d) {
        return new Tile(TreeSet.of(d));
    }

    //Can be memoized from the actual map :)
    private Tile discover(Vec2 p) {
        p = p.plus(startingPoint);
        return realMaze[p.x][p.y];
    }

    void run() {
        Vec2 initial = new Vec2(0,0);
        Tile initialTile = discover(initial);
        State initialState = new State(initial, initialTile, N);

        Tuple2<State, List<BreadCrumb>> globalState = Tuple.of(initialState, List.empty());
        for (; globalState != null; globalState = iterate(globalState));
    }

    Tuple2<State, List<BreadCrumb>> iterate(Tuple2<State, List<BreadCrumb>> globalState) {
        State s = globalState._1;
        List<BreadCrumb> bc = globalState._2;

        debug(globalState);

        if (s.succesors.canMoveTo.isEmpty()) { //We reached a dead end
            if (bc.isEmpty()) { //and there is nothing left to check
                System.out.println("Should end now");
                return null;
            }

            return backtrack(bc, s.facing).orElse(null);
        } else {
            Direction d = chooseDirection(s.succesors.canMoveTo, s.facing);
            if (!bc.isEmpty() || s.succesors.canMoveTo.size() > 1) {
                BreadCrumb newBreadCrumb = s.succesors.canMoveTo.size() == 1 ?
                        new BreadCrumb(tile(d.invert())) :
                        new BreadCrumb(s.succesors.excluding(d), s.position);
                bc = bc.push(newBreadCrumb);
            }

            return Tuple.of(transition(s.position, d), bc);
        }

    }

    private void debug(Tuple2<State, List<BreadCrumb>> globalState) {
        System.out.println("<" + globalState._1 + ", " + globalState._2 + ">");
    }

    Optional<Tuple2<State, List<BreadCrumb>>> backtrack(List<BreadCrumb> breadCrumbs, Direction facing) {

        System.out.println("    Backtracking " + breadCrumbs);
        if (breadCrumbs.isEmpty()) {
            return Optional.empty();
        }

        Tuple2<BreadCrumb, List<BreadCrumb>> popResult = breadCrumbs.pop2();
        breadCrumbs = popResult._2;
        BreadCrumb bc = popResult._1;
        Direction chosenDirection = chooseDirection(bc.successors.canMoveTo, facing); //can create a specific one for backtracking
        if (bc.position != null) { //See if this is needed
            if (bc.successors.canMoveTo.size() > 1) { //In case we still need to return to places that *AFAW* are only reachable from here (to optimize, we can work on finding other ways to reach a neighbor without revisiting this tile)
                breadCrumbs.push(new BreadCrumb(bc.successors.excluding(chosenDirection), bc.position));
            }
            //Go-to instead of transition? only go to pending places
            return Optional.of(Tuple.of(transition(bc.position, chosenDirection), breadCrumbs));
        } else {
            return backtrack(breadCrumbs, facing);
        }

    }

    //Should we always exclude the inverse of the current D? I think the breadcrumbs cover us here...
    private State transition(Vec2 position, Direction chosenDirection) {
        System.out.println("    transition -> " + chosenDirection);
        Vec2 newPosition = chosenDirection.displace(position);
        return new State(newPosition, discover(newPosition).excluding(chosenDirection.invert()), chosenDirection);
    }

    //Can be made fancier to account for current facing direction
    private Direction chooseDirection(SortedSet<Direction> canMoveTo, Direction facing) {
        System.out.println("choosing between " + canMoveTo + " facing: " + facing);
        return canMoveTo.contains(facing) ? facing : canMoveTo.get();
    }

    static class State {
        public final Vec2 position;
        public final Tile succesors;
        public final Direction facing;

        public State(Vec2 position, Tile succesors, Direction facing) {
            this.position = position;
            this.succesors = succesors;
            this.facing = facing;
        }

        @Override
        public String toString() {
            return MessageFormat.format("<{0}, {1}, {2}>", position, succesors.canMoveTo, facing);
        }
    }

    //Only breadcrumbs with remaining unexplored directions should
    //have a position, because we only care about revisiting those, but
    //in order to create an animatable route, the default inverse-based
    //backtracking suffices to provide a route, albeit non-optimal in some
    //cases... to optimize this, breadcrumbs should be smarter...
    //not sure if it is better to just save the breadcrumbs for cells with
    //unexplored neighbors (and defer to search) or use some kind of optimization
    //upon the existing breadcrumb, which has the advantage of not requiring
    //extra computation in order to find a viable route (it is calculated for free)
    static class BreadCrumb {
        public final Vec2 position;
        public final Tile successors;

        public BreadCrumb(Tile successors) {
            this(successors, null);
        }

        public BreadCrumb(Tile successors, Vec2 position) {
            this.position = position;
            this.successors = successors;
        }

        @Override
        public String toString() {
            return "<" + (position == null ? "" : position + ", " )+ successors.canMoveTo + ">"  ;
        }
    }
}
