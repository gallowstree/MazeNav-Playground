package sample;

import io.vavr.collection.SortedSet;

public interface MappingListener {
    void tileVisited(Vec2 pos);
    void tileDiscovered(Vec2 pos, SortedSet<Direction> walls);
}
