package sample;

import io.vavr.collection.List;
import io.vavr.collection.SortedSet;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import java.io.PrintStream;
import java.util.*;

import static io.vavr.collection.TreeSet.empty;
import static sample.Direction.*;
import static sample.Utils.degrees;
import static sample.Utils.rotate;

public class StrategyRunner extends MazeRenderer implements MappingListener {
    private Vec2 position;
    private Vec2 startPos;
    private Direction direction;

    private MappingStrategyV2 mapper;
    private Map<Vec2, Integer> visitedTiles = new HashMap<>();
    private SimpleAgent agent = new SimpleAgent();

    public StrategyRunner() {
        renderables.add(agent);
    }

    public void setup(MazeView m, Vec2 start, Direction facing, MappingStrategyV2 mappingStrategy) {
        position = start;
        direction = facing;

        mapper = mappingStrategy;
        this.startPos = start;

        setup(m);
    }

    protected void drawTileBackgrounds() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        visitedTiles.keySet().forEach(pos -> drawTileBackground(pos, gc));
    }

    private void drawTileBackground(Vec2 pos, GraphicsContext gc) {
        int visitedCount = visitedTiles.getOrDefault(pos, 0);

        gc.setFill(mazeBackgroundColor);
        for (int i = 0; i < visitedCount; i++)
            gc.setFill(((Color)gc.getFill()).saturate());

        double x1 = padding + pos.y * tileSize;
        double y1 = padding + pos.x * tileSize;

        double lineWidth = gc.getLineWidth() * 2;
        gc.fillRect(x1, y1, tileSize - lineWidth, tileSize - lineWidth);
    }

    protected void setupStage() {
        super.setupStage();
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
        agent.position = pos;
        agent.direction = d;
    }

    @Override
    public void tileDiscovered(Vec2 pos, SortedSet<Direction> walls) {
        if (!this.walls.containsKey(pos)) {
            this.walls.put(pos, empty());
        }
        this.walls.put(pos, this.walls.get(pos).union(walls));
    }

    private void drawPending(List<Vec2> pendingStack) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BISQUE);
        pendingStack.map(p -> p.plus(startPos)).forEach(p -> {
            gc.fillOval(p.y * tileSize + tileSize/2, p.x * tileSize + tileSize/2, tileSize/2, tileSize/2);
        });
    }

    public void run() {
        AnimationTimer timer = new AnimationTimer() {
            long prev;
            MappingStrategyV2.State s;
            @Override
            public void start() {
                super.start();
                mapper.setup(maze, position, direction);
                s = mapper.initial();
            }

            @Override
            public void handle(long now) {
                if (now - prev > 400000000) {
                    Optional<MappingStrategyV2.State> result = mapper.step(s);

                    if (!result.isPresent()) {
                        stop();
                        verifyAllTilesVisited(maze.realMaze);
                        return;
                    }
                    s = result.get();
                    drawMazeBackrgound();
                    drawTileBackgrounds();
                    drawWalls();
                    drawRenderables();
                    drawPending(s.pendingStack);
                    prev = now;

                }
            }
        };

        timer.start();
    }
}
