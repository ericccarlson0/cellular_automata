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

public class SimulationUI {
    private Simulation simulation;

    private static final String SPEED_PROMPT = "SPEED: ";
    public static final Color FONT_COLOR = Color.color(0.0, 0.0, 0.4);
    public static final int FONT_SIZE = 16;
    private static final double DEFAULT_NODE_SPACING = 12;
    public static final Color DISPLAY_COLOR = Color.color(0.9, 0.9, 1.0);
    private Stage stage;
    private Scene display;
    private GridPane topLevelGrid;
    private boolean shouldStep;
    private boolean isRunning;
    private ScrollPane scrollPane;
    private int delay;
    private int delayLeft;
    private static final int DEFAULT_SIM_DELAY = 20;
    public static final String RESOURCE_FOLDER = "/resources/";
    public static final String STYLESHEET = "default.css";
    private static final String DEFAULT_FONT = "Menlo";
    public static final int PADDING = 10;
    public static final int V_GAP = 10;
    public static final int H_GAP = 50;
    public static final int GRID_WIDTH = 600;
    public static final int GRID_HEIGHT = 600;
    public static final double DISPLAY_HEIGHT = 400;
    public static final double DISPLAY_WIDTH = 400;
    public static final double TOTAL_WIDTH = 550;
    public static final double TOTAL_HEIGHT = 550;
    private Slider mySlider;

    Locale locale = Locale.ENGLISH;
    ResourceBundle textElements = ResourceBundle.getBundle("resources.TextElements", locale);

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

        mySlider = new Slider(-2, 4, 0);
        mySlider.setLayoutX(300);
        mySlider.setSnapToTicks(true);
        mySlider.setMajorTickUnit(1.0f);
        mySlider.setBlockIncrement(0.5f);

        holder.getChildren().addAll(text, mySlider);
        return holder;
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

    private Text createPrompt(String prompt) {
        Text text = new Text(prompt);
        text.setFont(new Font(textElements.getString("font"), FONT_SIZE));
        text.setFill(FONT_COLOR);
        return text;
    }
}
