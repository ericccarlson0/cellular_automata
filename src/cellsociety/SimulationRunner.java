package cellsociety;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SimulationRunner extends Application {
    public static final String TITLE = "Simulation - Team 12";
    public static final int FRAMES_PER_SECOND = 60;
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final Rectangle NO_CURR_GRID = new Rectangle(600,600, Color.WHITE);

    private Grid currentGrid;
    private boolean shouldStep;
    private boolean isSimRunning;
    private int simDelay;
    private int currDelayLeft;
    private Stage simStage;
    private Scene simDisplay;
    private BorderPane displayHolder;
    private XMLParser fileParser;

    enum SimulationType{
        LIFE, FIRE, PERCOLATION, SEGREGATION, PREDPREY;
    }

    @Override
    public void start(Stage stage){
        fileParser = new XMLParser();
        simStage = stage;
        initializeUI();
        initializeVals();

        simStage.setScene(simDisplay);
        simStage.setTitle(TITLE);
        simStage.show();

        currentGrid = fileParser.generateGrid("./data/test.xml");
        displayHolder.setCenter(currentGrid.getGridVisual());

        KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step());
        Timeline animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
    }

    private void startButton(){
        isSimRunning = true;
    }

    private void stopButton(){
        isSimRunning = false;
    }

    private void initializeVals() {
        currentGrid = null;
        shouldStep = false;
        isSimRunning = false;
        simDelay = 10;
    }

    private void initializeUI() {
        Group root = new Group();
        displayHolder = new BorderPane();
        displayHolder.setPrefSize(700,800);

        HBox buttonHolder = new HBox();
        Button startButton = new Button("Start");
        startButton.setOnAction(event -> startButton());

        Button stopButton = new Button("Stop");
        stopButton.setOnAction(event -> stopButton());
        buttonHolder.getChildren().addAll(startButton,stopButton);

        displayHolder.setBottom(buttonHolder);

        root.getChildren().add(displayHolder);
        simDisplay = new Scene(root,700,800, Color.WHEAT);

        //TODO set up scene in simDisplay private variable - USE A BORDER PANE!
    }

    private void step(){
        if(currentGrid != null && (isSimRunning || shouldStep)){
            if(currDelayLeft > 0){
                currDelayLeft--;
            }
            else{
                currentGrid.step();
                if(shouldStep){
                    shouldStep = false;
                }
                currDelayLeft = simDelay;
            }
        }
    }

    public static void main(String[] args){
        launch(args);
    }
}
