package sample;

import io.vavr.collection.SortedSet;
import io.vavr.collection.TreeSet;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static io.vavr.collection.TreeSet.empty;
import static sample.Direction.*;

public class StrategyRunner implements MappingListener{

    private Stage stage;
    private double tileSize = 90;
    private double padding = 10;
    private double frameW, frameH;
    private Canvas canvas;
    private Vec2 position;
    private Direction direction;
    private Tile[][] maze;
    private MappingStrategy mapper;
    private Map<Vec2, Integer> visitedTiles = new HashMap<>();
    private Map<Vec2, SortedSet<Direction>> walls = new HashMap<>();

    public StrategyRunner(Stage stage) {
        this.stage = stage;
    }

    public void setup(Object[][] m, Vec2 start, Direction facing, MappingStrategy mappingStrategy) {
        position = start;
        direction = facing;
        maze = (Tile[][])m;
        mapper = mappingStrategy;

        frameW = maze[0].length * tileSize;
        frameH = maze.length * tileSize;

        setupStage();
        drawMazeBackrgound();
        drawAgent();

    }

    private void drawAgent() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.GREENYELLOW);

        double centerX = padding + position.y * tileSize + tileSize / 2;
        double centerY = padding + position.x * tileSize + tileSize / 2;
        double halfAgentBase = tileSize * 0.4 / 2;
        double halfAgentHeight = tileSize * 0.5 / 2;
        gc.save();
        rotate(gc, degrees(direction), centerX, centerY);
        gc.fillPolygon(new double[]{centerX - halfAgentBase, centerX + halfAgentBase, centerX},
                       new double[]{centerY + halfAgentHeight, centerY + halfAgentHeight, centerY - halfAgentHeight },3);
        gc.restore();
    }

    private double degrees(Direction direction) {
        return direction == N ? 0 :
               direction == S ? 180 :
               direction == E ? 90 : -90;
    }

    private void rotate(GraphicsContext gc, double angle, double px, double py) {
        Rotate r = new Rotate(angle, px, py);
        gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
    }

    private void drawMazeBackrgound() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.ALICEBLUE);
        gc.fillRect(padding, padding, frameW, frameH);
    }

    private void drawTiles() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        walls.keySet().forEach(pos -> {
            walls.get(pos).forEach(d -> drawWall(pos, d, gc));
        });
    }

    private void drawWall(Vec2 pos, Direction d, GraphicsContext gc) {
        gc.setStroke(Color.BLACK);
        double x1 = padding + (pos.y + (d == E  ? 1 : 0) ) * tileSize;
        double y1 = padding + (pos.x + (d != S  ? 0 : 1) ) * tileSize;
        double x2 = (d == E || d == W) ? x1 : x1 + tileSize;
        double y2 = (d == N || d == S) ? y1 : y1 + tileSize;
        gc.strokeLine(x1, y1, x2, y2);
    }

    private void setupStage() {
        Group root = new Group();
        stage.setScene(new Scene(root, frameW + padding*2, frameH + padding*2));
        canvas = new Canvas(frameW + padding, frameH + padding);
        root.getChildren().add(canvas);

        stage.getScene().setOnKeyPressed(event -> { run(); });

    }

    private void verifyAllTilesVisited(Tile[][] maze) {
        boolean allVisited = (Arrays.stream(maze)
                .allMatch(row -> Arrays.stream(row).allMatch(t -> t.visited)));
        String msg = allVisited ?
                "All tiles visited" : "Some tiles were not visited";
        PrintStream s = allVisited ? System.out : System.err;
        s.println(msg);
    }

    @Override
    public void tileVisited(Vec2 pos, Direction d) {
        if (!visitedTiles.containsKey(pos)) {
            visitedTiles.put(pos, 0);
        }
        visitedTiles.put(pos, visitedTiles.get(pos) + 1);
        position = pos;
        direction = d;
    }

    @Override
    public void tileDiscovered(Vec2 pos, SortedSet<Direction> walls) {
        if (!this.walls.containsKey(pos)) {
            this.walls.put(pos, empty());
        }
        this.walls.put(pos, this.walls.get(pos).union(walls));
    }

    public void run() {
        AnimationTimer timer = new AnimationTimer() {
            long prev;
            MappingStrategy.GlobalState s;
            @Override
            public void start() {
                super.start();
                s = mapper.setup(maze, position, direction);
            }

            @Override
            public void handle(long now) {
                if (now - prev > 300000000) {
                    s = mapper.step(s);
                    drawMazeBackrgound();
                    drawTiles();
                    drawAgent();
                    prev = now;
                    if (s == null) {
                        stop();
                        verifyAllTilesVisited(maze);
                    }
                }
            }
        };

        timer.start();
    }
}
