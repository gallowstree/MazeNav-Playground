package sample;

import javafx.application.Application;
import javafx.stage.Stage;

import static sample.Direction.*;
import static sample.Direction.W;
import static sample.Utils.tile;

public class Main extends Application {

    Tile[][] maze1 = {
        new Tile[] {tile(E),    tile(S,W),    tile(S,E),      tile(E,W), tile(S,W)},
        new Tile[] {tile(S,E),  tile(N,W),    tile(N,S),      tile(S),   tile(N)},
        new Tile[] {tile(N,E),  tile(E,W),    tile(N,S,W),    tile(N,E), tile(S, W)},
        new Tile[] {tile(E),    tile(E,W),    tile(N,E,W),    tile(E,W), tile(N, W)},
    };


    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Hello World");
        primaryStage.show();
        StrategyRunner runner = new StrategyRunner(primaryStage);
        runner.setup(maze1, new Vec2(1,2), N, new MappingStrategy(runner));
        runner.run();
    }


    public static void main(String[] args) {
        launch(args);
    }

}
