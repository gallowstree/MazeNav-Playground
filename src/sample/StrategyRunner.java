package sample;

import io.vavr.collection.SortedSet;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import java.io.PrintStream;
import java.util.Arrays;

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

    public StrategyRunner(Stage stage) {
        this.stage = stage;
    }

    public void setup(Tile[][] m, Vec2 start, Direction facing, MappingStrategy mappingStrategy) {
        position = start;
        direction = facing;
        maze = m;
        mapper = mappingStrategy;

        frameW = maze[0].length * tileSize;
        frameH = maze.length * tileSize;

        setupStage();
        drawMazeBackrgound();
        drawAgent();
    }

    private void drawAgent() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.DARKBLUE);

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

    private void setupStage() {
        Group root = new Group();
        stage.setScene(new Scene(root, frameW + padding*2, frameH + padding*2));
        canvas = new Canvas(frameW + padding, frameH + padding);
        root.getChildren().add(canvas);
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
    public void tileVisited(Vec2 pos) {

    }

    @Override
    public void tileDiscovered(Vec2 pos, SortedSet<Direction> walls) {

    }

    public void run() {
        mapper.run(maze, position, direction);
        verifyAllTilesVisited(maze);
    }
}
