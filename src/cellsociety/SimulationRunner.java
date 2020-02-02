package cellsociety;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
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

        Grid testing = fileParser.generateGrid("./data/test.xml");
        displayHolder.setCenter(testing.getGridVisual());

        KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step());
        Timeline animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
    }

    private void initializeVals() {
        currentGrid = null;
        shouldStep = false;
        isSimRunning = false;
        simDelay = 0;
    }

    private void initializeUI() {
        Group root = new Group();
        displayHolder = new BorderPane();
        displayHolder.setPrefSize(700,800);
        root.getChildren().add(displayHolder);
        simDisplay = new Scene(root,700,800, Color.WHEAT);
        //TODO set up scene in simDisplay private variable - USE A BORDER PANE!
    }

    private void step(){
        if(currentGrid != null && (isSimRunning || shouldStep)){
            try {
                Thread.sleep(simDelay);
            }
            catch(Exception e){
                System.out.println("Couldn't Delay");
            }
            currentGrid.step();
            if(shouldStep){
                shouldStep = false;
            }
        }
    }

    public static void main(String[] args){
        launch(args);
    }
}
