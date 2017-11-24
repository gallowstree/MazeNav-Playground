package sample;

import io.vavr.collection.SortedSet;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static sample.Direction.*;

public class MazeRenderer {
    protected Stage stage;
    protected Tile[][] maze;
    protected double tileSize = 90;
    protected double padding = 10;
    protected double frameW, frameH;
    protected Canvas canvas;
    protected Paint mazeBackgroundColor = Color.ALICEBLUE;
    protected Map<Vec2, SortedSet<Direction>> walls = new HashMap<>();
    protected List<Renderable> renderables = new ArrayList<>();

    public MazeRenderer(Stage stage) {
        this.stage = stage;
    }

    public void setup(Tile[][] m) {
        maze = m;

        frameW = maze[0].length * tileSize;
        frameH = maze.length * tileSize;

        setupStage();
        drawMazeBackrgound();
        drawWalls();
        drawRenderables();
    }

    protected void setupStage() {
        Group root = new Group();
        stage.setScene(new Scene(root, frameW + padding*2, frameH + padding*2));
        canvas = new Canvas(frameW + padding, frameH + padding);
        root.getChildren().add(canvas);
    }

    protected void drawMazeBackrgound() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(mazeBackgroundColor);
        gc.fillRect(padding, padding, frameW, frameH);
    }

    protected void drawWalls() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        walls.keySet().forEach(pos -> {
            walls.get(pos).forEach(d -> drawWall(pos, d, gc));
        });
    }

    protected void drawWall(Vec2 pos, Direction d, GraphicsContext gc) {
        gc.setStroke(Color.BLACK);
        double x1 = padding + (pos.y + (d == E  ? 1 : 0) ) * tileSize;
        double y1 = padding + (pos.x + (d != S  ? 0 : 1) ) * tileSize;
        double x2 = (d == E || d == W) ? x1 : x1 + tileSize;
        double y2 = (d == N || d == S) ? y1 : y1 + tileSize;
        gc.strokeLine(x1, y1, x2, y2);
    }

    //If you take the renderables as a parameter, you now have layers (depth)
    protected void drawRenderables() {
        renderables.forEach(r -> r.render(canvas.getGraphicsContext2D(), padding, tileSize));
    }

}
