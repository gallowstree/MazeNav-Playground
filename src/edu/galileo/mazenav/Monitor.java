package edu.galileo.mazenav;

import com.sun.tools.javac.util.List;
import edu.galileo.mazenav.animation.SteppedAnimation;
import edu.galileo.mazenav.antonio.AntonioControl;
import edu.galileo.mazenav.antonio.AntonioEventListener;
import edu.galileo.mazenav.antonio.AntonioSocketControl;
import edu.galileo.mazenav.common.Direction;
import edu.galileo.mazenav.common.MazeView;
import edu.galileo.mazenav.common.Vec2;
import edu.galileo.mazenav.rendering.MazeRenderer;
import edu.galileo.mazenav.search.AStarSearch;
import edu.galileo.mazenav.search.BfsSearch;
import edu.galileo.mazenav.search.DfsSearch;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import edu.galileo.mazenav.search.SearchAlgorithm;

import java.io.IOException;

import static edu.galileo.mazenav.common.Direction.N;

public class Monitor {

    private AntonioControl antonioControl = new AntonioSocketControl();

    private MazeView mazeView = emptyMazeView();
    private StrategyRunner mappingRenderer = new StrategyRunner();
    private StrategyRunner simulatorRenderer = new StrategyRunner();
    private AntonioEventListener eventListener;

    private List<SearchAlgorithm> searchAlgorithms = List.of(new DfsSearch(), new BfsSearch(), new AStarSearch());
    private List<SteppedAnimation> simulations = List.of(simulatorRenderer);

    //JavaFX UI STUFF
    private Stage stage;
    private BorderPane pane = new BorderPane();
    private final TextField txtIp = new TextField("192.168.1.8");
    private final Button btnConnect = new Button("Connect");
    private final VBox mappingBox = createMappingVBox();
    private final VBox simmulationBox = createSimmulationVBox();
    private final AnchorPane mappingPane = createMappingPane(mappingRenderer.getRoot());
    private final AnchorPane simulationPane = createMappingPane(simulatorRenderer.getRoot());

    private MazeView emptyMazeView() {
        return new MazeView(Main.emptyMaze11, new Vec2(5, 5), new MappingListener() {});
    }

    public Monitor() {
        initStage();
    }

    public Stage getStage() {
        return stage;
    }

    private void initStage() {
        this.stage = new Stage();
        BorderPane pane = createMainPane();
        stage.setScene(new Scene(pane, 1000, 800));
    }

    private BorderPane createMainPane() {
        HBox hbox = ModeBox();
        pane.setTop(hbox);

        mappingMode();

        return pane;
    }

    private void mappingMode() {
        pane.setLeft(mappingBox);
        pane.setCenter(mappingPane);
        mappingRenderer.setup(mazeView, new Vec2(5,5), N, null);
    }

    private void simulationMode() {
        pane.setLeft(simmulationBox);
        pane.setCenter(simulationPane);
    }

    private void startMapping() {
        try {
            antonioControl.startMapping();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Text newTitle(String text) {
        Text title = new Text(text);
        title.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        return title;
    }

    private VBox createSimmulationVBox() {
        VBox vbox = newLeftVBox();

        Text titleConn = newTitle("Simulations");
        ListView<SteppedAnimation> lstAnimations = new ListView<>();

        Button btnSimulate = new Button("Start Simulation");
        btnSimulate.setOnAction(event -> {
            simulatorRenderer.setup(mazeView, new Vec2(5,5), N, new MappingStrategyV2());
            simulatorRenderer.runAtOwnSpeed();
        });

        vbox.getChildren().addAll(titleConn, lstAnimations, btnSimulate);
        return vbox;
    }

    private VBox createMappingVBox() {
        VBox vbox = newLeftVBox();

        Text titleConn = newTitle("Connectivity");

        HBox connectivityBox = new HBox();
        connectivityBox.setSpacing(8);
        connectivityBox.getChildren().addAll(txtIp, btnConnect);
        btnConnect.setOnAction(event -> {
            try {
                eventListener = new AntonioEventListener(4421);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            new Thread(() -> {
                try {
                    eventListener.mappingListener = this.mappingRenderer;
//                    this.mappingRenderer.setup(mazeView, new Vec2(5,5), N, null);
                    this.mappingRenderer.startPos = new Vec2(10,10);
                    eventListener.run();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        });

        Text title = newTitle("Mapping");

        Button btnStart = new Button("Start Mapping");
        btnStart.setMaxWidth(Double.MAX_VALUE);
        btnStart.setOnAction(e -> startMapping());

        Button btnSendMap = new Button("Send Map");
        btnSendMap.setMaxWidth(Double.MAX_VALUE);

        Text titleSearch = newTitle("Search");

        Button btnSearch = new Button("Start Search...");
        btnSearch.setMaxWidth(Double.MAX_VALUE);

        ListView<SearchAlgorithm> lstSearch = new ListView<>();
        lstSearch.getItems().addAll(searchAlgorithms);

        vbox.getChildren().addAll(titleConn,
                connectivityBox,
                title,
                btnStart,
                btnSendMap,
                titleSearch,
                btnSearch,
                lstSearch);
        return vbox;
    }

    private VBox newLeftVBox() {
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(8);
        return vbox;
    }

    private HBox ModeBox() {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);
        hbox.setStyle("-fx-background-color: #336699;");

        Button buttonExplore = new Button("Control");
        buttonExplore.setPrefSize(100, 20);
        buttonExplore.setOnAction(event -> mappingMode());

        Button buttonProjected = new Button("Simulator");
        buttonProjected.setPrefSize(100, 20);
        buttonProjected.setOnAction(event -> simulationMode());

        Button buttonEdit = new Button("Map Editor");
        buttonEdit.setPrefSize(100, 20);

        hbox.getChildren().addAll(buttonExplore, buttonProjected, buttonEdit);

        return hbox;
    }

    private AnchorPane createMappingPane(Parent grid) {
        AnchorPane anchorpane = new AnchorPane();
        Button buttonSave = new Button("Save");
        Button buttonCancel = new Button("Cancel");

        HBox hb = new HBox();
        hb.setPadding(new Insets(0, 10, 10, 10));
        hb.setSpacing(10);
        hb.getChildren().addAll(buttonSave, buttonCancel);

        anchorpane.getChildren().addAll(grid,hb);   // Add grid from Example 1-5
        AnchorPane.setBottomAnchor(hb, 8.0);
        AnchorPane.setRightAnchor(hb, 5.0);
        AnchorPane.setTopAnchor(grid, 10.0);

        return anchorpane;
    }
}
