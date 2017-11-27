package edu.galileo.mazenav;

import edu.galileo.mazenav.common.Direction;
import edu.galileo.mazenav.common.Vec2;
import io.vavr.collection.SortedSet;

public interface MappingListener {

    default void tileVisited(Vec2 pos, Direction d) {}

    default void tileDiscovered(Vec2 pos, SortedSet<Direction> walls) {}
}
