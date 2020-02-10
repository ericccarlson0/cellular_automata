package cellsociety;

import cellsociety.backend.Cell;
import cellsociety.backend.gridstructures.GridStructure;
import cellsociety.frontend.*;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

public class SimulationUI {
    private Simulation simulation;

    private static final String SPEED_PROMPT = "SPEED: ";
    public static final Color FONT_COLOR = Color.color(0.0, 0.0, 0.4);
    public static final int FONT_SIZE = 16;
    private static final double DEFAULT_NODE_SPACING = 12;
    public static final int TOTAL_WIDTH = 600;
    public static final int TOTAL_HEIGHT = 600;
    public static final Color DISPLAY_COLOR = Color.color(0.9, 0.9, 1.0);
    private Stage whereRunning;
    private Scene simDisplay;
    private GridPane topLevelGrid;
    private boolean shouldStep;
    private boolean isSimRunning;
    private ScrollPane scrollPane;
    private int delay;
    private int delayLeft;
    private static final int DEFAULT_SIM_DELAY = 20;
    public static final String RESOURCE_FOLDER = "/resources/";
    public static final String STYLESHEET = "default.css";
    private static final String DEFAULT_FONT = "Menlo";
    public static final int PADDING = 5;
    public static final int V_GAP = 10;
    public static final int H_GAP = 50;
    private static final double SCROLL_PANE_HEIGHT = 400;
    private static final double SCROLL_PANE_WIDTH = 400;
    private Slider mySlider;

    Locale locale = Locale.ENGLISH;
    ResourceBundle textElements = ResourceBundle.getBundle("resources.TextElements", locale);

    public SimulationUI(Simulation sim, Stage stageForNewSim) {
        whereRunning = stageForNewSim;
        simulation = sim;
        initializeVariables();
        initUI();
    }

    private void initializeVariables() {
        shouldStep = false;
        isSimRunning = false;
        delay = DEFAULT_SIM_DELAY;
    }

    private void initUI() {
        Group root = new Group();
        topLevelGrid = initializePane();

        scrollPane = new ScrollPane();
        scrollPane.setContent(simulation.getDisplay());
        scrollPane.setPrefHeight(SCROLL_PANE_HEIGHT);
        scrollPane.setPrefWidth(SCROLL_PANE_WIDTH);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        topLevelGrid.add(scrollPane, 1, 0);

        HBox topButtons = setupTopButtons();
        topLevelGrid.add(topButtons, 1, 1);

        HBox centerButtons = setupCenterButtons();
        topLevelGrid.add(centerButtons, 1, 2);

        root.getChildren().add(topLevelGrid);
        simDisplay = new Scene(root, TOTAL_WIDTH, TOTAL_HEIGHT, DISPLAY_COLOR);

        String stylesheet = String.format("%s%s", RESOURCE_FOLDER, STYLESHEET);
        simDisplay.getStylesheets().add(getClass().getResource(stylesheet).toExternalForm());

        whereRunning.setScene(simDisplay);
        whereRunning.setTitle(textElements.getString("title"));
        whereRunning.show();
    }


    private GridPane initializePane() {
        GridPane pane = new GridPane();
        pane.setPadding(new Insets(PADDING, PADDING, PADDING, PADDING));
        pane.setVgap(V_GAP);
        pane.setHgap(H_GAP);
        return pane;
    }

    public void step() {
        if ((isSimRunning || shouldStep)){
            if (delayLeft > 0){
                delayLeft--;
            } else {
                checkSlider();
                simulation.step();
                if (shouldStep) {
                    shouldStep = false;
                } else {
                    delayLeft = delay;
                }
            }
        }
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
        delayLeft = 0;
    }

    private HBox setupTopButtons() {
        HBox buttonHolder = new HBox(DEFAULT_NODE_SPACING);

        Button startButton = new Button(textElements.getString("start"));
        startButton.setOnAction(event -> startButton());
        Button stopButton = new Button(textElements.getString("stop"));
        stopButton.setOnAction(event -> stopButton());
        Button stepButton = new Button(textElements.getString("step"));
        stepButton.setOnAction(event -> stepButton());

        buttonHolder.getChildren().addAll(startButton, stopButton, stepButton);
        return buttonHolder;
    }

    private void checkSlider() {
        double sliderValue = mySlider.getValue();
        double spd = discretize(sliderValue);
        delay = (int)(DEFAULT_SIM_DELAY * Math.pow(2, spd));
    }

    private double discretize(double continuousVal) {
        double discreteVal = -1 * ((int) (2*continuousVal)) / 2;
        return discreteVal;
    }

    private HBox setupCenterButtons() {
        HBox buttonHolder = new HBox(DEFAULT_NODE_SPACING);

        Text prompt = new Text(SPEED_PROMPT);
        prompt.setFont(new Font(DEFAULT_FONT, FONT_SIZE));
        prompt.setFill(FONT_COLOR);

        mySlider = new Slider(-4, 4, 0);
        mySlider.setLayoutX(300);
        mySlider.setSnapToTicks(true);
        mySlider.setMajorTickUnit(1.0f);
        mySlider.setBlockIncrement(0.5f);

        buttonHolder.getChildren().addAll(prompt, mySlider);
        return buttonHolder;
    }
}
