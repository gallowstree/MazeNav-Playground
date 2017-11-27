package edu.galileo.mazenav.common;


import io.vavr.collection.SortedSet;

public class Tile {
    //Should be immutable
    public final SortedSet<Direction> canMoveTo;

    public boolean visited;

    public Tile(SortedSet<Direction> canMoveTo) {
        this.canMoveTo = canMoveTo;
    }

    public Tile excluding(Direction d) {
        return new Tile(canMoveTo.remove(d));
    }

    @Override
    public String toString() {
        return "Tile{" +
                "canMoveTo=" + canMoveTo +
                '}';
    }
}
