package edu.galileo.mazenav;

import edu.galileo.mazenav.animation.SteppedAnimation;
import edu.galileo.mazenav.common.Direction;
import edu.galileo.mazenav.common.MazeView;
import edu.galileo.mazenav.common.Tile;
import edu.galileo.mazenav.common.Vec2;
import edu.galileo.mazenav.rendering.MazeRenderer;
import edu.galileo.mazenav.rendering.SimpleAgent;
import io.vavr.collection.List;
import io.vavr.collection.SortedSet;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.io.PrintStream;
import java.util.*;

import static io.vavr.collection.TreeSet.empty;

public class StrategyRunner extends MazeRenderer implements MappingListener, SteppedAnimation<MappingStrategyV2.State> {
    private Vec2 position;
    public Vec2 startPos;
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

        m.setMappingListener(this);
        setup(m);
    }

    @Override
    protected void drawTileBackgrounds() {
        super.drawTileBackgrounds();
        visitedTiles.keySet().forEach(this::drawTileBackground);
    }

    @Override
    protected void drawTileBackground(Vec2 pos) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
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
        draw();
    }

    @Override
    public void tileDiscovered(Vec2 pos, SortedSet<Direction> walls) {
        if (!this.walls.containsKey(pos)) {
            this.walls.put(pos, empty());
        }
        this.walls.put(pos, this.walls.get(pos).union(walls));
        draw();
    }

    private void draw() {
        drawMazeBackrgound();
        drawTileBackgrounds();
        drawWalls();
        drawRenderables();
    }

    private void drawPending(List<Vec2> pendingStack) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BISQUE);
        pendingStack.map(p -> p.plus(startPos)).forEach(p -> {
            gc.fillOval(p.y * tileSize + tileSize/2, p.x * tileSize + tileSize/2, tileSize/2, tileSize/2);
        });
    }

    @Override
    public MappingStrategyV2.State initial() {
        return mapper.initial();
    }

    @Override
    public Optional<MappingStrategyV2.State> step(MappingStrategyV2.State curr) {
        return null;
    }

    public void runAtOwnSpeed() {
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
