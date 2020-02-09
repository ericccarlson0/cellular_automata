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

    public static final String TITLE = "Cellular Automata";
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
    public static final int TOTAL_WIDTH = 750;
    public static final int TOTAL_HEIGHT = 750;
    public static final int DISPLAY_WIDTH = 400;
    public static final int DISPLAY_HEIGHT = 400;
    private static final int DEFAULT_SIM_DELAY = 20;
    private static final String FILE_ERROR_MESSAGE = "The filename you entered is either invalid or could not be found.";
    private static final String START_SIM_MESSAGE = "Press Start to enjoy the Simulation!";
    private static final String DEFAULT_INFOBOX_MESSAGE = "Load a simulation by entering its filename.";
    private static final String DEFAULT_FONT = "Menlo";

    private Rectangle noCurrGrid = new Rectangle(DISPLAY_WIDTH, DISPLAY_HEIGHT,
            Color.color(0.2, 0.2, .6));
    private String myShape;
    private boolean shouldStep;
    private boolean simRunning;
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
        simRunning = false;
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

        VBox messageBoxes = new VBox();
        messageBoxes.getChildren().addAll(createInfoBox(), setupShapeButtons(), createStatsBox()); //***
        topLevelGrid.add(messageBoxes, 2, 1);

        root.getChildren().add(topLevelGrid);
        simDisplay = new Scene(root, TOTAL_WIDTH, TOTAL_HEIGHT, DISPLAY_COLOR);

        String stylesheet = String.format("%s%s", RESOURCE_FOLDER, STYLESHEET);
        simDisplay.getStylesheets().add(getClass().getResource(stylesheet).toExternalForm());
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
        HBox buttonHolder = new HBox();

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
        HBox buttonHolder = new HBox();

        Text prompt = new Text("Speed: ");
        prompt.setFont(new Font(DEFAULT_FONT, FONT_SIZE));
        prompt.setFill(FONT_COLOR);

        mySlider = new Slider(-4, 4, 0);
        mySlider.setLayoutX(300);
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

        Text prompt = new Text("Enter filename: ");
        prompt.setFont(new Font(DEFAULT_FONT, FONT_SIZE));
        prompt.setFill(FONT_COLOR);
        myTextField = new TextField();
        // myTextField.setPrefColumnCount(20); //***

        Button loadButton = new Button("Load");
        loadButton.setOnAction(event -> loadButton());

        buttonHolder.getChildren().addAll(prompt, myTextField, loadButton);
        return buttonHolder;
    }

    private VBox setupShapeButtons() {
        VBox buttonHolder = new VBox();
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
        simRunning = true;
    }

    private void stopButton() {
        simRunning = false;
    }

    private void stepButton() {
        simRunning = false;
        shouldStep = true;
        delayLeft = 0;
    }

    private void shapeButton() {
        myShape = myShapeButtons.getSelectedToggle().getUserData().toString();
    }

    private void loadButton() {
        simRunning = false;
        topLevelGrid.getChildren().remove(noCurrGrid);
        if (currSimulation != null)
            topLevelGrid.getChildren().remove(currSimulation.getDisplay()); //***
         try {
            XMLFilename = String.format("%s%s", XML_FOLDER, myTextField.getText());
            generateSimulation();
            scrollPane.setContent(currSimulation.getDisplay());
            addMessage(myInfoBox, START_SIM_MESSAGE);
         } catch (Exception e) {
            currSimulation = null;
            scrollPane.setContent(noCurrGrid);
            addMessage(myInfoBox, FILE_ERROR_MESSAGE);
         }
    }

    private void generateSimulation() {
        GridStructure gs = fileParser.generateGrid(XMLFilename);
        currSimulation = new Simulation(gs, myShape);
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
        if (currSimulation != null && (simRunning || shouldStep)){
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
