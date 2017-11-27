package edu.galileo.mazenav.rendering;

import javafx.scene.canvas.GraphicsContext;

public interface Renderable {
    default void render(GraphicsContext gc) {}

    default void render(GraphicsContext gc, double padding, double tileSize) {}
}
