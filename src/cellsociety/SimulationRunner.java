package cellsociety;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SimulationRunner extends Application {

    public static final String TITLE = "SIMULATION -- TEAM 12";
    public static final int FRAMES_PER_SECOND = 60;
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;

    public static final String RESOURCE_FOLDER = "/resources/";
    public static final String STYLESHEET = "default.css";
    public static final Color DISPLAY_COLOR = Color.WHEAT;
    public static final Rectangle NO_CURR_GRID = new Rectangle(500,500,
            Color.color(0.2, 0.2, .2));

    public static final int PADDING = 5;
    public static final int V_GAP = 10;
    public static final int H_GAP = 50;
    public static final int TOTAL_WIDTH = 800;
    public static final int TOTAL_HEIGHT = 800;

    private Grid currentGrid;
    private boolean shouldStep;
    private boolean isSimRunning;
    private int simDelay;
    private int currDelayLeft;
    private Stage simStage;
    private Scene simDisplay;
    private BorderPane displayPane;
    private GridPane gridPane;
    private XMLParser fileParser;
    private String XMLFilename;
    private TextField myTextField;
    private Slider mySlider;

    enum SimulationType {
        LIFE, FIRE, PERCOLATION, SEGREGATION, PRED_PREY;
    }

    @Override
    public void start (Stage stage) {
        fileParser = new XMLParser();
        simStage = stage;
        initializeUI();
        initializeVariables();

        simStage.setScene(simDisplay);
        simStage.setTitle(TITLE);
        simStage.show();

        Text title = new Text(TITLE);
        title.setFill(Color.BLACK);

        gridPane.add(title, 1, 0);

        gridPane.add(NO_CURR_GRID, 1, 1);

        KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step());
        Timeline animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
    }

    private void initializeVariables() {
        currentGrid = null;
        shouldStep = false;
        isSimRunning = false;
        simDelay = 10;
    }

    private void initializeUI() {
        Group root = new Group();
        gridPane = initializePane();
        displayPane = new BorderPane();

        HBox upperButtons = setupUpperButtons();
        HBox centerButtons = setupCenterButtons();
        HBox bottomButtons = setupBottomButtons();

        gridPane.add(upperButtons, 1, 2);
        gridPane.add(centerButtons, 1, 3);
        gridPane.add(bottomButtons, 1, 4);

        displayPane.setCenter(gridPane);
        root.getChildren().add(displayPane);
        simDisplay = new Scene(root,TOTAL_WIDTH,TOTAL_HEIGHT, DISPLAY_COLOR);
        simDisplay.getStylesheets().add(getClass().getResource(RESOURCE_FOLDER + STYLESHEET).toExternalForm());
    }

    private GridPane initializePane() {
        GridPane pane = new GridPane();
        pane.setPadding(new Insets(PADDING, PADDING, PADDING, PADDING));
        pane.setVgap(V_GAP);
        pane.setHgap(H_GAP);
        return pane;
    }

    private HBox setupUpperButtons() {
        HBox buttonHolder = new HBox();

        Button startButton = new Button("START");
        startButton.setOnAction(event -> startButton());

        Button stopButton = new Button("STOP");
        stopButton.setOnAction(event -> stopButton());

        Button stepButton = new Button("STEP");
        stepButton.setOnAction(event -> stepButton());

        buttonHolder.getChildren().addAll(startButton, stopButton, stepButton);
        return buttonHolder;
    }
    private HBox setupCenterButtons() {
        HBox buttonHolder = new HBox();

        Text prompt = new Text("SPEED: ");

        mySlider = new Slider(-4, 4, 0);
        mySlider.setLayoutX(200);
        mySlider.setShowTickMarks(true);
        // mySlider.setShowTickLabels(true);
        mySlider.setSnapToTicks(true);
        mySlider.setMajorTickUnit(1.0f);
        mySlider.setBlockIncrement(0.5f);

        buttonHolder.getChildren().addAll(prompt, mySlider);
        return buttonHolder;
    }
    private HBox setupBottomButtons() {
        HBox buttonHolder = new HBox();

        Text prompt = new Text("FILENAME: ");
        myTextField = new TextField();
        myTextField.setPrefColumnCount(10);

        Button loadButton = new Button("LOAD");
        loadButton.setOnAction(event -> loadButton());

        buttonHolder.getChildren().addAll(prompt, myTextField, loadButton);
        return buttonHolder;
    }

    private void startButton() {
        isSimRunning = true;
    }
    private void stopButton() {
        isSimRunning = false;
    }
    private void stepButton() {
        isSimRunning = false;
        shouldStep = true;
        currDelayLeft = 0;
    }
    private void loadButton() {
        isSimRunning = false; //***

        XMLFilename = String.format("data/%s", myTextField.getText());
        currentGrid = fileParser.generateGrid(XMLFilename);
        gridPane.add(currentGrid.getGridVisual(), 1, 1);
    }

    private void step() {
        if (currentGrid != null && (isSimRunning || shouldStep)){
            if (currDelayLeft > 0){
                currDelayLeft--;
            } else {
                checkSlider();
                currentGrid.step();
                if (shouldStep) {
                    shouldStep = false;
                } else {
                    currDelayLeft = simDelay;
                }
            }
        }
    }

    private void checkSlider() {
        double sliderValue = mySlider.getValue();
        int speed = discrete(sliderValue);
        simDelay = (int) (10 * Math.pow(2, speed));
    }

    private int discrete(double speed) {
        if (speed < -3.5) {
            return 4;
        } else if (speed < -2.5) {
            return 3;
        } else if (speed < -1.5) {
            return 2;
        } else if (speed < -0.5) {
            return 1;
        } else if (speed < 0.5) {
            return 0;
        } else if (speed < 1.5) {
            return -1;
        } else if (speed < 2.5) {
            return -2;
        } else if (speed < 3.5) {
            return -3;
        } else {
            return -4;
        }
    }

    public static void main(String[] args){
        launch(args);
    }
}
