package cellsociety;

import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * SimulationUI is a class that holds a Simulation object, which it accesses to update and display when the step()
 * function is called.
 * 'display' is the Scene that is passed to its Stage, 'stage'
 * 'topLevelGrid' is the GridPane that holds the rendering of the cell matrix in addition to all of the UI controls
 * 'slider' is the Slider UI control that controls the speed of the simulation
 * In addition, there are 'start', 'stop', and 'step' buttons that are used to control the flow of the simulation.
 */
public class SimulationUI {

    public static final Color FONT_COLOR = Color.color(0.0, 0.0, 0.4);
    public static final Color DISPLAY_COLOR = Color.color(0.9, 0.9, 1.0);
    public static final int FONT_SIZE = 16;
    private static final double DEFAULT_NODE_SPACING = 12;
    private Stage stage;
    private Scene display;
    private GridPane topLevelGrid;
    private boolean shouldStep;
    private boolean isRunning;
    private int delay;
    private int delayLeft;
    private static final int DEFAULT_SIM_DELAY = 20;
    public static final String RESOURCE_FOLDER = "/resources/";
    public static final String STYLESHEET = "default.css";
    public static final int PADDING = 10;
    public static final int V_GAP = 10;
    public static final int H_GAP = 50;
    public static final int GRID_WIDTH = 600;
    public static final int GRID_HEIGHT = 600;
    public static final double DISPLAY_WIDTH = 400;
    public static final double DISPLAY_HEIGHT = 400;
    public static final double TOTAL_WIDTH = 550;
    public static final double TOTAL_HEIGHT = 550;
    private Slider slider;

    private Simulation simulation;
    Locale locale = Locale.ENGLISH;
    ResourceBundle textElements = ResourceBundle.getBundle("resources.TextElements", locale);

    /**
     * The constructor for each SimulationUI initializes variables and then calls initUI() -- this method sets up
     * the display (the cell matrix and the UI controls), adds the display to the stage, and shows the stage.
     * @param sim:  A Simulation object is created and passed to the SimulationUI in order for it to be updated and
     *           displayed. As of now, this object can only be created by an XMLParser in SimulationMenu, but it does
     *           not seem to be too much of a stretch to create it using only UI controls that the user selects.
     * @param stageForNewSim:   This is the stage object passed so that SimulationUI may act as an independent pop-
     *                      up window.
     */
    public SimulationUI(Simulation sim, Stage stageForNewSim) {
        stage = stageForNewSim;
        simulation = sim;
        initializeVariables();
        initUI();
    }

    private void initializeVariables() {
        shouldStep = false;
        isRunning = false;
        delay = DEFAULT_SIM_DELAY;
    }

    private void initUI() {
        Group root = new Group();
        topLevelGrid = initializeTopGrid();

        topLevelGrid.add(initializeScrollPane(), 1, 1);
        topLevelGrid.add(setupStateButtons(), 1, 2);
        topLevelGrid.add(setupSpeedControl(), 1, 3);

        root.getChildren().add(topLevelGrid);
        display = new Scene(root, TOTAL_WIDTH, TOTAL_HEIGHT, DISPLAY_COLOR);

        String stylesheet = String.format("%s%s", RESOURCE_FOLDER, STYLESHEET);
        display.getStylesheets().add(getClass().getResource(stylesheet).toExternalForm());

        stage.setScene(display);
        stage.setTitle(textElements.getString("title"));
        stage.show();
    }


    private GridPane initializeTopGrid() {
        GridPane pane = new GridPane();
        pane.setPadding(new Insets(PADDING, PADDING, PADDING, PADDING));
        pane.setVgap(V_GAP);
        pane.setHgap(H_GAP);
        return pane;
    }

    private ScrollPane initializeScrollPane() {
        ScrollPane ret = new ScrollPane();
        ret.setId("simulation-UI");
        ret.setContent(simulation.getDisplay());
        ret.setPrefHeight(DISPLAY_HEIGHT);
        ret.setPrefWidth(DISPLAY_WIDTH);
        ret.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        ret.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        return ret;
    }

    /**
     * The step() is called from the Timeline in SimulationMenu at the end of each frame. It depends on a 'delay',
     * which decreases on each step and prevents an update occurring up to when it becomes zero. This delay is what
     * is actually changed when the "speed" of the simulation is changed -- it becomes half of what it used to be
     * when 2X speed is selected, for example.
     * The function uses isRunning to determine whether the function is active, and shouldStep to determine if the
     * 'step' button has been pressed within the last frame.
     */
    public void step() {
        if ((isRunning || shouldStep)){
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
        isRunning = true;
    }

    private void stopButton() {
        isRunning = false;
    }

    private void stepButton() {
        isRunning = false;
        shouldStep = true;
        delayLeft = 0;
    }

    private HBox setupStateButtons() {
        HBox holder = new HBox(DEFAULT_NODE_SPACING);

        Button startButton = new Button(textElements.getString("start"));
        startButton.setOnAction(event -> startButton());
        Button stopButton = new Button(textElements.getString("stop"));
        stopButton.setOnAction(event -> stopButton());
        Button stepButton = new Button(textElements.getString("step"));
        stepButton.setOnAction(event -> stepButton());

        holder.getChildren().addAll(startButton, stopButton, stepButton);
        return holder;
    }

    private HBox setupSpeedControl() {
        HBox holder = new HBox(DEFAULT_NODE_SPACING);
        Text text = createPrompt(textElements.getString("speedPrompt"));

        slider = new Slider(-2, 4, 0);
        slider.setLayoutX(300);
        slider.setSnapToTicks(true);
        slider.setMajorTickUnit(1.0f);
        slider.setBlockIncrement(0.5f);

        holder.getChildren().addAll(text, slider);
        return holder;
    }

    private void checkSlider() {
        double sliderValue = slider.getValue();
        double spd = discretize(sliderValue);
        delay = (int)(DEFAULT_SIM_DELAY * Math.pow(2, spd));
    }

    private double discretize(double continuousVal) {
        double discreteVal = -1 * ((int) (2*continuousVal)) / 2;
        return discreteVal;
    }

    private Text createPrompt(String prompt) {
        Text text = new Text(prompt);
        text.setFont(new Font(textElements.getString("font"), FONT_SIZE));
        text.setFill(FONT_COLOR);
        return text;
    }
}
