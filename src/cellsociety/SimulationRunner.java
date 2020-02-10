package cellsociety;

import cellsociety.backend.gridstructures.GridStructure;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SimulationRunner extends Application {

    public static final String TITLE = "CELLULAR AUTOMATA";
    public static final int FRAMES_PER_SECOND = 60;
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;

    public static final String RESOURCE_FOLDER = "/resources/";
    public static final String STYLESHEET = "default.css";
    public static final String XML_FOLDER = "./data/";
    public static final Color DISPLAY_COLOR = Color.color(0.9, 0.9, 1.0);
    public static final Color FONT_COLOR = Color.color(0.0, 0.0, 0.4);
    public static final int FONT_SIZE = 16;
    public static final String SQUARE = "SQUARE";
    public static final String CIRCLE = "CIRCLE";
    public static final String DIAMOND = "DIAMOND";
    public static final String TRIANGLE = "TRIANGLE";
    public static final String HEXAGON = "HEXAGON";

    public static final int PADDING = 5;
    public static final int V_GAP = 10;
    public static final int H_GAP = 50;
    public static final int BOX_WIDTH = 100;
    public static final int TOTAL_WIDTH = 1000;
    public static final int TOTAL_HEIGHT = 750;
    public static final int DISPLAY_WIDTH = 500;
    public static final int DISPLAY_HEIGHT = 500;
    private static final double SCROLL_PANE_HEIGHT = 400;
    private static final double SCROLL_PANE_WIDTH = 400;
    private static final double DEFAULT_NODE_SPACING = 12;
    private static final int BUTTON_SPACING = 4;
    private static final int DEFAULT_SIM_DELAY = 20;
    private static final String FILE_ERROR_MESSAGE = "The filename you entered is either invalid or could not be found.";
    private static final String START_SIM_MESSAGE = "Press Start to enjoy the Simulation!";
    private static final String DEFAULT_INFOBOX_MESSAGE = "Load a simulation by entering its filename.";
    private static final String DEFAULT_FONT = "Menlo";
    private static final String TORUS = "TORUS";
    private static final String NO_TORUS = "FLAT GRID";
    private static final String FILENAME_PROMPT = "FILENAME: ";
    private static final String SPEED_PROMPT = "SPEED: ";
    private static final String USER_INPUT_PROMPT = "PRESS TO LOAD CURRENT INPUT:"; //***

    private Rectangle noCurrGrid = new Rectangle(DISPLAY_WIDTH, DISPLAY_HEIGHT,
            Color.color(0.2, 0.2, .6));
    private String myShape;
    private boolean shouldStep;
    private boolean isSimRunning;
    private boolean isTorus = false;
    private int delay;
    private int delayLeft;

    private Simulation currSimulation;
    private Stage simStage;
    private Scene simDisplay;
    private GridPane topLevelGrid;
    private ScrollPane scrollPane;

    private XMLParser fileParser;
    private String XMLFilename;
    private TextField myTextField;
    private Slider mySlider;
    private StackPane myInfoBox;
    private StackPane myStatsBox;
    private ToggleGroup myShapeButtons;
    private ToggleGroup myTorusButtons;

    enum SimulationType {
        LIFE, FIRE, PERCOLATION, SEGREGATION, PRED_PREY, RPS
    }

    @Override
    public void start (Stage stage) {
        fileParser = new XMLParser();
        simStage = stage;

        initializeUI();
        initializeVariables();

        Text title = new Text(TITLE);
        title.setFill(Color.BLACK);

        scrollPane = new ScrollPane();
        scrollPane.setContent(noCurrGrid);
        scrollPane.setPrefHeight(SCROLL_PANE_HEIGHT);
        scrollPane.setPrefWidth(SCROLL_PANE_WIDTH);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        topLevelGrid.add(title, 1, 0);
        topLevelGrid.add(scrollPane, 1, 1);
        // noCurrGrid.setArcWidth(20.0);
        // noCurrGrid.setArcHeight(20.0);

        simStage.setScene(simDisplay);
        simStage.setTitle(TITLE);
        simStage.show();

        KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step());
        Timeline animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
    }

    private void initializeVariables() {
        currSimulation = null;
        shouldStep = false;
        isSimRunning = false;
        delay = DEFAULT_SIM_DELAY;
    }

    private void initializeUI() {
        Group root = new Group();
        topLevelGrid = initializePane(); // topLevelGrid is the highest level grid

        HBox topButtons = setupTopButtons();
        topLevelGrid.add(topButtons, 1, 2);
        HBox centerButtons = setupCenterButtons();
        topLevelGrid.add(centerButtons, 1, 3);
        HBox bottomButtons = setupBottomButtons();
        topLevelGrid.add(bottomButtons, 1, 4);

        VBox messageBoxes = new VBox(DEFAULT_NODE_SPACING);
        messageBoxes.getChildren().addAll(createInfoBox(), createStatsBox(), setupShapeButtons()); //***
        topLevelGrid.add(messageBoxes, 2, 1);

        VBox torusButtons = setupTorusButtons();
        topLevelGrid.add(torusButtons, 2, 2);

        VBox userButtons = setupUserButtons();
        topLevelGrid.add(userButtons, 2, 3); //***

        root.getChildren().add(topLevelGrid);
        simDisplay = new Scene(root, TOTAL_WIDTH, TOTAL_HEIGHT, DISPLAY_COLOR);

        String stylesheet = String.format("%s%s", RESOURCE_FOLDER, STYLESHEET);
        simDisplay.getStylesheets().add(getClass().getResource(stylesheet).toExternalForm()); //***
        myInfoBox.getChildren().add(new Label("FILLER"));
        addMessage(myInfoBox, DEFAULT_INFOBOX_MESSAGE);
    }

    private GridPane initializePane() {
        GridPane pane = new GridPane();
        pane.setPadding(new Insets(PADDING, PADDING, PADDING, PADDING));
        pane.setVgap(V_GAP);
        pane.setHgap(H_GAP);
        return pane;
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

    private HBox setupCenterButtons() {
        HBox buttonHolder = new HBox(DEFAULT_NODE_SPACING);

        Text prompt = new Text(SPEED_PROMPT);
        prompt.setFont(new Font(DEFAULT_FONT, FONT_SIZE));
        prompt.setFill(FONT_COLOR);

        mySlider = new Slider(-4, 4, 0);
        mySlider.setLayoutX(300);
//        mySlider.setShowTickMarks(false);
//        mySlider.setShowTickLabels(false);
        mySlider.setSnapToTicks(true);
        mySlider.setMajorTickUnit(1.0f);
        mySlider.setBlockIncrement(0.5f);

        buttonHolder.getChildren().addAll(prompt, mySlider);
        return buttonHolder;
    }

    private HBox setupBottomButtons() {
        HBox holder = new HBox(DEFAULT_NODE_SPACING);

        Text prompt = new Text(FILENAME_PROMPT);
        prompt.setFont(new Font(DEFAULT_FONT, FONT_SIZE));
        prompt.setFill(FONT_COLOR);
        myTextField = new TextField();
        // myTextField.setPrefColumnCount(20); //***

        Button xmlLoadButton = new Button("Load XML File");
        xmlLoadButton.setOnAction(event -> xmlLoadButton());

        holder.getChildren().addAll(prompt, myTextField, xmlLoadButton);
        return holder;
    }

    private VBox setupUserButtons() {
        VBox holder = new VBox(DEFAULT_NODE_SPACING);

        Text prompt = new Text(USER_INPUT_PROMPT);
        prompt.setFont(new Font(DEFAULT_FONT, FONT_SIZE));
        prompt.setFill(FONT_COLOR);

        Button userLoadButton = new Button("   Load   ");
        userLoadButton.setOnAction(event -> userLoadButton());

        holder.getChildren().addAll(prompt, userLoadButton);
        return holder;
    }

    private VBox setupShapeButtons() {
        VBox buttonHolder = new VBox(BUTTON_SPACING);
        ToggleGroup tg = new ToggleGroup();
        myShapeButtons = tg;

        String[] shapeArray = new String[]{SQUARE, DIAMOND, TRIANGLE, HEXAGON, CIRCLE};
        for (String shape: shapeArray){
            RadioButton button = new RadioButton(shape);
            button.setUserData(shape);
            button.setToggleGroup(tg);
            button.setOnAction(event -> shapeButton());
            buttonHolder.getChildren().add(button);
        }
        return buttonHolder;
    }

    private VBox setupTorusButtons() {
        VBox holder = new VBox(BUTTON_SPACING);
        ToggleGroup tg = new ToggleGroup();
        myTorusButtons = tg;

        String[] torusArray = new String[]{TORUS, NO_TORUS};
        for (String option: torusArray){
            RadioButton button = new RadioButton(option);
            button.setUserData(option);
            button.setToggleGroup(tg);
            button.setOnAction(event -> torusButton());
            holder.getChildren().add(button);
        }
        return holder;
    }

    private StackPane createStatsBox() {
        StackPane statsBox = createMessageBox(BOX_WIDTH);
        myStatsBox = statsBox;
        return statsBox;
    }

    private StackPane createInfoBox() {
        StackPane infoBox = createMessageBox(BOX_WIDTH);
        myInfoBox = infoBox;
        return infoBox;
    }

    private StackPane createMessageBox(int size) {
        StackPane sp = new StackPane();
        Rectangle background = new Rectangle(size*2, size, Color.WHITE);
        background.setArcWidth(BOX_WIDTH/10);
        background.setArcHeight(BOX_WIDTH/10);
        sp.getChildren().add(background);
        return sp;
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

    private void shapeButton() {
        myShape = myShapeButtons.getSelectedToggle().getUserData().toString();
    }
    private void torusButton() {
        isTorus = (myTorusButtons.getSelectedToggle().getUserData().toString()
                == TORUS);
    }

    private void xmlLoadButton() {
        isSimRunning = false;
        topLevelGrid.getChildren().remove(noCurrGrid);
        if (currSimulation != null)
            topLevelGrid.getChildren().remove(currSimulation.getDisplay()); //***
         // try {
            XMLFilename = String.format("%s%s", XML_FOLDER, myTextField.getText());
            generateSimFromFilename();
            scrollPane.setContent(currSimulation.getDisplay());
            addMessage(myInfoBox, START_SIM_MESSAGE);
//         } catch (Exception e) {
//            currSimulation = null;
//            scrollPane.setContent(noCurrGrid);
//            addMessage(myInfoBox, FILE_ERROR_MESSAGE);
//         }
    }

    private void generateSimFromFilename() {
        GridStructure gs = fileParser.generateGrid(XMLFilename);
        currSimulation = new Simulation(gs, myShape);
    }

    private void userLoadButton() {
        isSimRunning = false;
        topLevelGrid.getChildren().remove(noCurrGrid);
        if (currSimulation != null)
            topLevelGrid.getChildren().remove(currSimulation.getDisplay()); //***
        // try {
        generateSimFromCurrentInput();
        scrollPane.setContent(currSimulation.getDisplay());
        addMessage(myInfoBox, START_SIM_MESSAGE);
//      } catch (Exception e) {
//         currSimulation = null;
//         scrollPane.setContent(noCurrGrid);
//         addMessage(myInfoBox, FILE_ERROR_MESSAGE);
//      }
    }

    private void generateSimFromCurrentInput(){

    }

    private void addMessage (Pane messageBox, String message) {
        Label l = new Label(message);
        l.setFont(new Font(DEFAULT_FONT, FONT_SIZE/2)); //***
        l.setWrapText(true);
        l.setMaxWidth(BOX_WIDTH);
        if (messageBox.getChildren().size() > 0) {
            messageBox.getChildren().remove(1);
        }
        messageBox.getChildren().add(l);
    }

    private void step() {
        if (currSimulation != null && (isSimRunning || shouldStep)){
            if (delayLeft > 0){
                delayLeft--;
            } else {
                checkSlider();
                currSimulation.step();
                if (shouldStep) {
                    shouldStep = false;
                } else {
                    delayLeft = delay;
                }
            }
        }
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

    public static void main(String[] args){
        launch(args);
    }
}
