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
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import static cellsociety.SimulationRunner.TITLE;

public class Simulation {
    private static final String SPEED_PROMPT = "SPEED: ";
    public static final Color FONT_COLOR = Color.color(0.0, 0.0, 0.4);
    public static final int FONT_SIZE = 16;
    private static final double DEFAULT_NODE_SPACING = 12;
    public static final double DISPLAY_HEIGHT = 500;
    public static final double DISPLAY_WIDTH = 500;
    public static final int TOTAL_WIDTH = 600;
    public static final int TOTAL_HEIGHT = 600;
    public static final Color DISPLAY_COLOR = Color.color(0.9, 0.9, 1.0);
    private GridStructure gridStruct;
    private GridDisplay gridDisplay;
    private int rowNum;
    private int colNum;
    private String cellShape;
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
    public static final int PADDING = 5;
    public static final int V_GAP = 10;
    public static final int H_GAP = 50;
    private static final double SCROLL_PANE_HEIGHT = 400;
    private static final double SCROLL_PANE_WIDTH = 400;
    private Slider mySlider;
    private static final String DEFAULT_FONT = "Menlo";

    public enum AllStates {
        LIFE_ALIVE(Color.BLACK),
        LIFE_EMPTY(Color.WHITE),
        FIRE_TREE(Color.color(0.2, 0.75, 0.2)),
        FIRE_FIRE(Color.color(0.8, 0.2, 0.0)),
        FIRE_EMPTY(Color.color(0.8, 0.8, 0.6)),
        PERCOLATION_FULL(Color.color(0.5, 0.75, 1.0)),
        PERCOLATION_EMPTY(Color.color(1.0, 1.0, 1.0)),
        PERCOLATION_BLOCK(Color.color(0.4, 0.2, 0.2)),
        SEGREGATION_ONE(Color.color(1.0, 0.5, 0.5)),
        SEGREGATION_TWO(Color.color(0.5, 0.5, 1.0)),
        SEGREGATION_EMPTY(Color.color(1.0, 1.0, 1.0)),
        PRED_PREY_FISH(Color.LIGHTCORAL),
        PRED_PREY_SHARK(Color.BLACK),
        PRED_PREY_EMPTY(Color.TURQUOISE),
        RPS_ROCK(Color.color(0.0, 0.0, 0.0)),
        RPS_PAPER(Color.color(1.0, 1.0, 1.0)),
        RPS_SCISSORS(Color.color(0.5, 0.5, 0.5)),
        ANT_EMPTY(Color.WHITE),
        ANT_PHEROMONES(Color.LIGHTGREEN),
        ANT_FOOD(Color.BLUEVIOLET),
        ANT_NEST(Color.ORANGERED),
        ANT_FULL(Color.DARKGREEN);


        private Color stateColor;
        AllStates(Color p) { stateColor = p; }

        public Color getColor() {
            return stateColor;
        }
    }

    public Simulation(GridStructure gs, String shape, Stage stageForNewSim) {
        whereRunning = stageForNewSim;
        gridStruct = gs;
        rowNum = gs.getRowNum();
        colNum = gs.getColNum();
        cellShape = shape;
        initDisplay();
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
        scrollPane.setContent(getDisplay());
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
        whereRunning.setTitle(TITLE);
        whereRunning.show();
    }


    private GridPane initializePane() {
        GridPane pane = new GridPane();
        pane.setPadding(new Insets(PADDING, PADDING, PADDING, PADDING));
        pane.setVgap(V_GAP);
        pane.setHgap(H_GAP);
        return pane;
    }

    private void initDisplay() {
        selectInitGrid();
        for (int row=0; row<rowNum; row++) {
            for (int col=0; col<colNum; col++){
                Cell currCell = gridStruct.getCellAtIndex(row, col);
                gridDisplay.addCellToDisplay(row, col, currCell.getCurrState());
            }
        }
    }

    private void selectInitGrid(){
        if (cellShape == "DIAMOND") {
            gridDisplay = new DiamondDisplay(rowNum, colNum);
        } else if (cellShape == "TRIANGLE") {
            gridDisplay = new TriangleDisplay(rowNum, colNum);
        } else if (cellShape == "HEXAGON") {
            gridDisplay = new HexagonDisplay(rowNum, colNum);
        } else if (cellShape == "CIRCLE") {
            gridDisplay = new CircleDisplay(rowNum, colNum);
        } else {
            gridDisplay = new SquareDisplay(rowNum, colNum);
        }
    }

    public Region getDisplay() {
        return gridDisplay.getDisplay();
    }

    public void step() {
        if ((isSimRunning || shouldStep)){
            if (delayLeft > 0){
                delayLeft--;
            } else {
                checkSlider();
                stepSim();
                if (shouldStep) {
                    shouldStep = false;
                } else {
                    delayLeft = delay;
                }
            }
        }
    }

    private void stepSim() {
        gridStruct.step();
        updateDisplay();
    }

    private void updateDisplay() {
        for (int row = 0; row < rowNum; row++) {
            for (int col = 0; col < colNum; col++) {
                gridDisplay.updateDisplayAtCell(row, col, gridStruct.getStateAtCell(row, col));
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

        Button startButton = new Button("Start");
        startButton.setOnAction(event -> startButton());
        Button stopButton = new Button("Stop");
        stopButton.setOnAction(event -> stopButton());
        Button stepButton = new Button("Step");
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
