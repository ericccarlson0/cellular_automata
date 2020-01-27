package cellsociety;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SimulationRunner extends Application {
    public static final String TITLE = "Simulation - Team 12";
    public static final int FRAMES_PER_SECOND = 60;
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;

    private Grid currentGrid;
    private boolean shouldStep;
    private boolean isSimRunning;
    private int simSpeed;
    private Stage simStage;
    private Scene simDisplay;

    @Override
    public void start(Stage stage){
        simStage = stage;
        initializeUI();

        simStage.setScene(simDisplay);
        simStage.setTitle(TITLE);
        simStage.show();

        KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step());
        Timeline animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
    }

    private void initializeUI() {
        //TODO set up scene in simDisplay private variable
    }

    private void step(){
        if(isSimRunning || shouldStep){
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
