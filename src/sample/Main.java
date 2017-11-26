package sample;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


import static sample.Direction.*;
import static sample.Utils.tile;

public class Main extends Application {

    public static Tile[][] maze2 = {
        new Tile[] {tile(E),    tile(S,W),    tile(S,E),      tile(E,W), tile(S,W)},
        new Tile[] {tile(S,E),  tile(N,W),    tile(N,S),      tile(S),   tile(N)},
        new Tile[] {tile(N,E),  tile(E,W),    tile(N,S,W,E),    tile(N,E,W), tile(S, W)},
        new Tile[] {tile(E),    tile(E,W),    tile(N,E,W),    tile(E,W), tile(N, W)},
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

        Button btnControl   = new Button("Controlar carro de verga");
        Button btnEditor    = new Button("Editar laVERGinto");
        Button btnSimulator = new Button("Simular algoritmos de verga");

        btnControl.setOnAction(event -> newControlWindow());

        final VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(0, 10, 0, 10));
        vbox.getChildren().addAll(btnControl, btnEditor, btnSimulator);


        primaryStage.setScene(new Scene(vbox, 800, 300));
        primaryStage.show();

//        StrategyRunner runner = new StrategyRunner(primaryStage);
//        runner.setup(maze2, new Vec2(0,1), E, new MappingStrategyV2(runner));


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
