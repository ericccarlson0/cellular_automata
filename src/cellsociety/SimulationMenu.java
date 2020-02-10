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
import java.util.HashMap;

import java.util.Locale;
import java.util.ResourceBundle;

public class SimulationMenu extends Application {

    public static final int FRAMES_PER_SECOND = 60;
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;

    public static final String RESOURCE_FOLDER = "/resources/"; // [hard coded because not locale-specific]
    public static final String STYLESHEET = "default.css";
    public static final String XML_FOLDER = "./data/";
    public static final Color DISPLAY_COLOR = Color.color(0.9, 0.9, 1.0);
    public static final Color FONT_COLOR = Color.color(0.0, 0.0, 0.4);
    public static final int FONT_SIZE = 16;
    public static final int PADDING = 5;
    public static final int V_GAP = 10;
    public static final int H_GAP = 50;
    public static final int BOX_WIDTH = 100;
    public static final int TOTAL_WIDTH = 1000;
    public static final int TOTAL_HEIGHT = 750;
    private static final double DEFAULT_NODE_SPACING = 12;
    private static final int BUTTON_SPACING = 4;
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
    HashMap<SimulationUI, Stage> allRunningSims;
    private int neighborhoodType;

    Locale locale = Locale.ENGLISH;
    ResourceBundle textElements = ResourceBundle.getBundle("resources.TextElements", locale);

    enum SimulationType {
        LIFE, FIRE, PERCOLATION, SEGREGATION, PRED_PREY, RPS, ANT
    }


    @Override
    public void start (Stage stage) {
        allRunningSims = new HashMap<>();
        fileParser = new XMLParser();
        simStage = stage;

        initializeUI();

        Text title = new Text(textElements.getString("title"));
        title.setFill(Color.BLACK);

        topLevelGrid.add(title, 1, 0);

        simStage.setScene(simDisplay);
        simStage.setTitle(textElements.getString("title"));
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
        addMessage(myInfoBox, textElements.getString("defaultInfoboxMessage"));
    }

    private GridPane initializePane() {
        GridPane pane = new GridPane();
        pane.setPadding(new Insets(PADDING, PADDING, PADDING, PADDING));
        pane.setVgap(V_GAP);
        pane.setHgap(H_GAP);
        return pane;
    }

    private HBox setupBottomButtons() {
        HBox holder = new HBox(DEFAULT_NODE_SPACING);

        Text prompt = new Text(textElements.getString("filenamePrompt"));
        prompt.setFont(new Font(textElements.getString("font"), FONT_SIZE));
        prompt.setFill(FONT_COLOR);
        myTextField = new TextField();

        Button loadButton = new Button("Load XML File");
        loadButton.setOnAction(event -> loadButton());

        holder.getChildren().addAll(prompt, myTextField, loadButton);
        return holder;
    }

    private VBox setupShapeButtons() {
        VBox buttonHolder = new VBox(BUTTON_SPACING);
        ToggleGroup tg = new ToggleGroup();
        myShapeButtons = tg;

        String[] shapeArray = new String[]{textElements.getString("square"),
                textElements.getString("diamond"), textElements.getString("triangle"),
                textElements.getString("hexagon"), textElements.getString("circle")};
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

        String[] torusArray = new String[]{textElements.getString("torus"),
                textElements.getString("noTorus")};
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
                == textElements.getString("torus"));
    }

    private void loadButton() {
        try {
            XMLFilename = String.format("%s%s", XML_FOLDER, myTextField.getText());
            generateSimulation();
            addMessage(myInfoBox, textElements.getString("defaultInfoboxMessage"));
        } catch (Exception e) {
            addMessage(myInfoBox, textElements.getString("fileErrorMessage"));
        }
    }

    private void generateSimulation() {
        GridStructure gs = fileParser.generateGrid(XMLFilename,isTorus,neighborhoodType);
        Stage stageForNewSim = new Stage();
        currSimulation = new Simulation(gs, myShape);
        SimulationUI sim = new SimulationUI(currSimulation,stageForNewSim);
        allRunningSims.put(sim,stageForNewSim);
    }

    private void addMessage (Pane messageBox, String message) {
        Label l = new Label(message);
        l.setFont(new Font(textElements.getString("font"), FONT_SIZE/2)); //***
        l.setWrapText(true);
        l.setMaxWidth(BOX_WIDTH);
        if (messageBox.getChildren().size() > 0) {
            messageBox.getChildren().remove(1);
        }
        messageBox.getChildren().add(l);
    }

    private void step() {
        for (SimulationUI currSim : allRunningSims.keySet()){
            currSim.step();
        }
    }

    public static void main(String[] args){
        launch(args);
    }
}
