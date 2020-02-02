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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SimulationRunner extends Application {

    public static final String TITLE = "SIMULATION - TEAM 12";
    public static final int FRAMES_PER_SECOND = 60;
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;

    public static final Color DISPLAY_COLOR = Color.WHEAT;
    public static final Rectangle NO_CURR_GRID = new Rectangle(500,500,
            Color.color(0.2, 0.2, .2));
    public static final String FONT = "menlo";

    public static final int PADDING = 5;
    public static final int GAP = 5;
    public static final int TOTAL_WIDTH = 800;
    public static final int TOTAL_HEIGHT = 800;

    private Grid currentGrid;
    private boolean shouldStep;
    private boolean isSimRunning;
    private int simDelay;
    private int currDelayLeft;
    private Stage simStage;
    private Scene simDisplay;
    private GridPane displayPane;
    private XMLParser fileParser;
    private String XMLFilename;
    private TextField tf;

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
        title.setFont(new Font(FONT, 20));
        displayPane.add(title, 1, 0);
        displayPane.add(NO_CURR_GRID, 1, 1);

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
        displayPane = initializePane();
        displayPane.setPrefSize(TOTAL_WIDTH,TOTAL_HEIGHT);

        HBox upperButtons = setupUpperButtons();
        HBox centerButtons = setupCenterButtons();
        HBox bottomButtons = setupBottomButtons();

        displayPane.add(upperButtons, 1, 2);
        displayPane.add(centerButtons, 1, 3);
        displayPane.add(bottomButtons, 1, 4);

        root.getChildren().add(displayPane);
        simDisplay = new Scene(root,TOTAL_WIDTH,TOTAL_HEIGHT, DISPLAY_COLOR);
    }

    private GridPane initializePane() {
        GridPane pane = new GridPane();
        pane.setPadding(new Insets(PADDING, PADDING, PADDING, PADDING));
        pane.setVgap(GAP);
        pane.setHgap(GAP);
        return pane;
    }

    private HBox setupUpperButtons() {
        HBox buttonHolder = new HBox();

        Button startButton = new Button("START");
        startButton.setFont(new Font(FONT, 12));
        startButton.setOnAction(event -> startButton());

        Button stopButton = new Button("STOP");
        stopButton.setFont(new Font(FONT, 12));
        stopButton.setOnAction(event -> stopButton());

        Button stepButton = new Button("STEP");
        stepButton.setFont(new Font(FONT, 12));
        stepButton.setOnAction(event -> stepButton());

        buttonHolder.getChildren().addAll(startButton, stopButton, stepButton);
        return buttonHolder;
    }

    private HBox setupCenterButtons() {
        HBox buttonHolder = new HBox();

        Text prompt = new Text("SPEED: ");
        prompt.setFont(new Font(FONT, 12));

        Slider speeds = new Slider(-4, 4, 0);
        speeds.setShowTickMarks(true);
        speeds.setShowTickLabels(true);
        speeds.setMajorTickUnit(1.0f);
        speeds.setBlockIncrement(0.5f);

        buttonHolder.getChildren().addAll(prompt, speeds);
        return buttonHolder;
    }

    private HBox setupBottomButtons() {
        HBox buttonHolder = new HBox();

        Text prompt = new Text("FILENAME: ");
        tf = new TextField();
        tf.setPrefColumnCount(10);

        Button loadButton = new Button("LOAD");
        loadButton.setOnAction(event -> loadButton());

        buttonHolder.getChildren().addAll(prompt, tf, loadButton);
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
        XMLFilename = tf.getText();
        currentGrid = fileParser.generateGrid(XMLFilename);
        displayPane.add(currentGrid.getGridVisual(), 1, 1);
    }

    private void step(){
        if (currentGrid != null && (isSimRunning || shouldStep)){
            if (currDelayLeft > 0){
                currDelayLeft--;
            } else {
                currentGrid.step();
                if (shouldStep) {
                    shouldStep = false;
                }
                else {
                    currDelayLeft = simDelay;
                }
            }
        }
    }

    public static void main(String[] args){
        launch(args);
    }
}
