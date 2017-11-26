package sample;

import com.sun.tools.javac.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.animation.SteppedAnimation;
import sample.search.AStarSearch;
import sample.search.BfsSearch;
import sample.search.DfsSearch;
import sample.search.SearchAlgorithm;

import java.io.IOException;

public class Monitor {

    private AntonioControl antonioControl = new AntonioSocketControl();
    private MazeRenderer renderer = new MazeRenderer();
    private BorderPane pane = new BorderPane();
    private MazeView mazeView = emptyMazeView();

    private List<SearchAlgorithm> searchAlgorithms = List.of(new DfsSearch(), new BfsSearch(), new AStarSearch());

    //UI STUFF
    private Stage stage;
    private final TextField txtIp = new TextField("192.168.1.8");
    private final Button btnConnect = new Button("Connect");
    private final VBox mappingBox = createMappingVBox();
    private final VBox simmulationBox = createSimmulationVBox();

    private MazeView emptyMazeView() {
        return new MazeView(Main.maze2, new Vec2(0, 0), new MappingListener() {});
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
        stage.setScene(new Scene(pane, 800, 600));
    }

    private BorderPane createMainPane() {
        HBox hbox = addHBox();
        pane.setTop(hbox);
        addStackPane(hbox);

        createMainArea();

        mappingMode();

        return pane;
    }

    private void createMainArea() {
        pane.setCenter(addAnchorPane(renderer.getRoot()));
        renderer.setup(mazeView);
    }

    private void mappingMode() {
        pane.setLeft(mappingBox);
    }

    private void simulationMode() {
        pane.setLeft(simmulationBox);
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

        vbox.getChildren().addAll(titleConn, lstAnimations);
        return vbox;
    }

    private VBox createMappingVBox() {
        VBox vbox = newLeftVBox();

        Text titleConn = newTitle("Connectivity");

        HBox connectivityBox = new HBox();
        connectivityBox.setSpacing(8);
        connectivityBox.getChildren().addAll(txtIp, btnConnect);

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

    private void addStackPane(HBox hb) {
        StackPane stack = new StackPane();
        Rectangle helpIcon = new Rectangle(30.0, 25.0);
        helpIcon.setFill(new LinearGradient(0,0,0,1, true, CycleMethod.NO_CYCLE,
                new Stop(0,Color.web("#4977A3")),
                new Stop(0.5, Color.web("#B0C6DA")),
                new Stop(1,Color.web("#9CB6CF"))));
        helpIcon.setStroke(Color.web("#D0E6FA"));
        helpIcon.setArcHeight(3.5);
        helpIcon.setArcWidth(3.5);

        Text helpText = new Text("?");
        helpText.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
        helpText.setFill(Color.WHITE);
        helpText.setStroke(Color.web("#7080A0"));

        stack.getChildren().addAll(helpIcon, helpText);
        stack.setAlignment(Pos.CENTER_RIGHT);     // Right-justify nodes in stack
        StackPane.setMargin(helpText, new Insets(0, 10, 0, 0)); // Center "?"

        hb.getChildren().add(stack);            // Add to HBox from Example 1-2
        HBox.setHgrow(stack, Priority.ALWAYS);    // Give stack any extra space
    }

    private HBox addHBox() {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);
        hbox.setStyle("-fx-background-color: #336699;");

        Button buttonExplore = new Button("Control");
        buttonExplore.setPrefSize(100, 20);

        Button buttonProjected = new Button("Simulator");
        buttonProjected.setPrefSize(100, 20);
        buttonProjected.setOnAction(event -> simulationMode());

        Button buttonEdit = new Button("Map Editor");
        buttonEdit.setPrefSize(100, 20);

        hbox.getChildren().addAll(buttonExplore, buttonProjected, buttonEdit);

        return hbox;
    }

    private AnchorPane addAnchorPane(Parent grid) {
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
