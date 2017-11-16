package sample;

import javafx.application.Application;
import javafx.stage.Stage;

import static sample.Direction.*;
import static sample.Utils.tile;

public class Main extends Application {

    Tile[][] maze2 = {
        new Tile[] {tile(E),    tile(S,W),    tile(S,E),      tile(E,W), tile(S,W)},
        new Tile[] {tile(S,E),  tile(N,W),    tile(N,S),      tile(S),   tile(N)},
        new Tile[] {tile(N,E),  tile(E,W),    tile(N,S,W),    tile(N,E), tile(S, W)},
        new Tile[] {tile(E),    tile(E,W),    tile(N,E,W),    tile(E,W), tile(N, W)},
    };
    /*Tile[][] maze1 = {
            new Tile[] {tile(E,S),    tile(E,W),    tile(S,W),      tile(S,E), tile(S,W)},
            new Tile[] {tile(N),  tile(E),    tile(N,E,W),      tile(N,W),   tile(N)},

    };

    Tile[][] maze2 = {
            new Tile[] {tile(S,E),    tile(W)},
            new Tile[] {tile(N,E),  tile(S,W)},
            new Tile[] {tile(S,E),  tile(N,S,W)},
            new Tile[] {tile(N,S),  tile(N)},
            new Tile[] {tile(N,E),  tile(E,W)},

    };
*/

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Hello World");
        primaryStage.show();
        StrategyRunner runner = new StrategyRunner(primaryStage);

        //Array<Object[]> transposed = List.transpose(List.of(List.of(maze1))).map(List::toJavaArray).toArray();

        runner.setup(maze2, new Vec2(0,1), E, new MappingStrategyV2(runner));
    }


    public static void main(String[] args) {
        launch(args);
    }

}
