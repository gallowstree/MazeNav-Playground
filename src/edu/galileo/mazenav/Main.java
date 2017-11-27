package edu.galileo.mazenav;

import edu.galileo.mazenav.common.Direction;
import edu.galileo.mazenav.common.Tile;
import javafx.application.Application;
import javafx.stage.Stage;

import static edu.galileo.mazenav.common.Utils.tile;

public class Main extends Application {

    public static Tile[][] maze2 = {
        new Tile[] {tile(Direction.E),    tile(Direction.S, Direction.W),    tile(Direction.S, Direction.E),      tile(Direction.E, Direction.W), tile(Direction.S, Direction.W)},
        new Tile[] {tile(Direction.S, Direction.E),  tile(Direction.N, Direction.W),    tile(Direction.N, Direction.S),      tile(Direction.S),   tile(Direction.N)},
        new Tile[] {tile(Direction.N, Direction.E),  tile(Direction.E, Direction.W),    tile(Direction.N, Direction.S, Direction.W, Direction.E),    tile(Direction.N, Direction.E, Direction.W), tile(Direction.S, Direction.W)},
        new Tile[] {tile(Direction.E),    tile(Direction.E, Direction.W),    tile(Direction.N, Direction.E, Direction.W),    tile(Direction.E, Direction.W), tile(Direction.N, Direction.W)},
    };

    public static Tile[][] emptyMaze11 = {
            {tile(),tile(),tile(),tile(),tile(),tile(),tile(),tile(),tile(),tile(), tile()},
            {tile(),tile(),tile(),tile(),tile(),tile(),tile(),tile(),tile(),tile(), tile()},
            {tile(),tile(),tile(),tile(),tile(),tile(),tile(),tile(),tile(),tile(), tile()},
            {tile(),tile(),tile(),tile(),tile(),tile(),tile(),tile(),tile(),tile(), tile()},
            {tile(),tile(),tile(),tile(),tile(),tile(),tile(),tile(),tile(),tile(), tile()},
            {tile(),tile(),tile(),tile(),tile(),tile(),tile(),tile(),tile(),tile(), tile()},
            {tile(),tile(),tile(),tile(),tile(),tile(),tile(),tile(),tile(),tile(), tile()},
            {tile(),tile(),tile(),tile(),tile(),tile(),tile(),tile(),tile(),tile(), tile()},
            {tile(),tile(),tile(),tile(),tile(),tile(),tile(),tile(),tile(),tile(), tile()},
            {tile(),tile(),tile(),tile(),tile(),tile(),tile(),tile(),tile(),tile(), tile()},
    };
    private Stage stage;
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
        this.stage = primaryStage;
        primaryStage.setTitle("MazeNav Playground V0.1");
        primaryStage.show();
        newControlWindow();
    }

    private void newControlWindow() {
        stage.hide();
        Stage controlStage = new Monitor().getStage();
        controlStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

}
