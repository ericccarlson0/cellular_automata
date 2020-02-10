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

import java.util.ArrayList;
import java.util.HashMap;

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
    public static final int TOTAL_WIDTH = 800;
    public static final int TOTAL_HEIGHT = 600;
    public static final int DISPLAY_WIDTH = 500;
    public static final int DISPLAY_HEIGHT = 500;
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

    private Rectangle noCurrGrid = new Rectangle(DISPLAY_WIDTH, DISPLAY_HEIGHT,
            Color.color(0.2, 0.2, .6));
    private String myShape;
    private boolean isTorus = false;

    private Simulation currSimulation;
    private Stage simStage;
    private Scene simDisplay;
    private GridPane topLevelGrid;

    private XMLParser fileParser;
    private String XMLFilename;
    private TextField myTextField;
    private StackPane myInfoBox;
    private StackPane myStatsBox;
    private ToggleGroup myShapeButtons;
    private ToggleGroup myTorusButtons;

    HashMap<Simulation,Stage> allRunningSims;

    enum SimulationType {
        LIFE, FIRE, PERCOLATION, SEGREGATION, PRED_PREY, RPS, ANT
    }

    @Override
    public void start (Stage stage) {
        allRunningSims = new HashMap<>();
        fileParser = new XMLParser();
        simStage = stage;

        initializeUI();

        Text title = new Text(TITLE);
        title.setFill(Color.BLACK);

        topLevelGrid.add(title, 1, 0);

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
        Group root = new Group();
        topLevelGrid = initializePane(); // topLevelGrid is the highest level grid

        HBox bottomButtons = setupBottomButtons();
        topLevelGrid.add(bottomButtons, 1, 4);

        VBox messageBoxes = new VBox(DEFAULT_NODE_SPACING);
        messageBoxes.getChildren().addAll(createInfoBox(), createStatsBox(), setupShapeButtons()); //***
        topLevelGrid.add(messageBoxes, 2, 1);

        VBox torusButtons = setupTorusButtons();
        topLevelGrid.add(torusButtons, 2, 2);

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

    private HBox setupBottomButtons() {
        HBox buttonHolder = new HBox(DEFAULT_NODE_SPACING);

        Text prompt = new Text(FILENAME_PROMPT);
        prompt.setFont(new Font(DEFAULT_FONT, FONT_SIZE));
        prompt.setFill(FONT_COLOR);
        myTextField = new TextField();

        Button loadButton = new Button("Load");
        loadButton.setOnAction(event -> loadButton());

        buttonHolder.getChildren().addAll(prompt, myTextField, loadButton);
        return buttonHolder;
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

    private void shapeButton() {
        myShape = myShapeButtons.getSelectedToggle().getUserData().toString();
    }

    private void torusButton() {
        isTorus = (myTorusButtons.getSelectedToggle().getUserData().toString()
                == TORUS);
    }

    private void loadButton() {
        try {
            XMLFilename = String.format("%s%s", XML_FOLDER, myTextField.getText());
            generateSimulation();
            addMessage(myInfoBox, START_SIM_MESSAGE);
        } catch (Exception e) {
            addMessage(myInfoBox, FILE_ERROR_MESSAGE);
        }
    }

    private void generateSimulation() {
        GridStructure gs = fileParser.generateGrid(XMLFilename,isTorus);
        Stage stageForNewSim = new Stage();
        currSimulation = new Simulation(gs, myShape, stageForNewSim);
        allRunningSims.put(currSimulation,stageForNewSim);
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
        for(Simulation currSim : allRunningSims.keySet()){
            currSim.step();
        }
    }

    public static void main(String[] args){
        launch(args);
    }
}
