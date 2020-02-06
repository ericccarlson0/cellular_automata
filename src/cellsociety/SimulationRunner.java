package cellsociety;

import cellsociety.backend.Cell;
import cellsociety.backend.gridstructures.GridStructure;
import cellsociety.frontend.GridDisplay;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
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
    public static final String SQUARE = cellsociety.backend.Cell.CellShape.SQUARE.name();
    public static final String CIRCLE = cellsociety.backend.Cell.CellShape.CIRCLE.name();
    public static final String DIAMOND = cellsociety.backend.Cell.CellShape.DIAMOND.name();

    public static final int PADDING = 5;
    public static final int V_GAP = 10;
    public static final int H_GAP = 50;
    public static final int BOX_WIDTH = 100;
    public static final int TOTAL_WIDTH = 800;
    public static final int TOTAL_HEIGHT = 800;
    private static final String FILE_ERROR_MESSAGE = "The filename you entered is either invalid or could not be found.";
    private static final String START_SIM_MESSAGE = "Press Start to enjoy the Simulation!";

    private Rectangle noCurrGrid = new Rectangle(500,500, Color.color(0.2, 0.2, .6));
    private String myShape = SQUARE;

    private GridStructure currGridStruct;
    private GridDisplay currGridDisplay;
    private boolean shouldStep;
    private boolean isSimRunning;
    private int simDelay;
    private int currDelayLeft;
    private Stage simStage;
    private Scene simDisplay;
    private BorderPane displayPane;
    private GridPane topGrid;
    private ScrollPane scrollPane;

    private XMLParser fileParser;
    private String XMLFilename;
    private TextField myTextField;
    private Slider mySlider;
    private StackPane myInfoBox;
    private StackPane myStatsBox;
    private ToggleGroup myShapeButtons;

    enum SimulationType {
        LIFE, FIRE, PERCOLATION, SEGREGATION, PRED_PREY
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

        topGrid.add(title, 1, 0);
        topGrid.add(scrollPane, 1, 1);
        noCurrGrid.setArcWidth(20.0);
        noCurrGrid.setArcHeight(20.0);

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
        currGridStruct = null;
        currGridDisplay = null;
        shouldStep = false;
        isSimRunning = false;
        simDelay = 10;
    }

    private void initializeUI() {
        Group root = new Group();
        topGrid = initializePane();
        displayPane = new BorderPane();

        HBox topButtons = setupTopButtons();
        topGrid.add(topButtons, 1, 2);
        HBox centerButtons = setupCenterButtons();
        topGrid.add(centerButtons, 1, 3);
        HBox bottomButtons = setupBottomButtons();
        topGrid.add(bottomButtons, 1, 4);

        GridPane boxes = new GridPane();
        boxes.add(createInfoBox(), 0, 0);
        boxes.add(setupShapeButtons(), 0, 1);
        boxes.add(createStatsBox(), 0, 2);

        topGrid.add(boxes, 2, 1);


        displayPane.setCenter(topGrid);
        root.getChildren().add(displayPane);
        simDisplay = new Scene(root, TOTAL_WIDTH, TOTAL_HEIGHT, DISPLAY_COLOR);
        simDisplay.getStylesheets().add(getClass().getResource(RESOURCE_FOLDER + STYLESHEET).toExternalForm());
        myInfoBox.getChildren().add(new Label(""));
        addMessage(myInfoBox,"Load a simulation by entering its filename.");
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
        prompt.setFont(new Font("Menlo", FONT_SIZE));
        prompt.setFill(FONT_COLOR);

        mySlider = new Slider(-4, 4, 0);
        mySlider.setLayoutX(200);
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
        prompt.setFont(new Font("Menlo", FONT_SIZE));
        prompt.setFill(FONT_COLOR);
        myTextField = new TextField();
        // myTextField.setPrefColumnCount(20); //***

        Button loadButton = new Button("Load");
        loadButton.setOnAction(event -> loadButton());

        buttonHolder.getChildren().addAll(prompt, myTextField, loadButton);
        return buttonHolder;
    }

    private GridPane setupShapeButtons() {
        GridPane buttonHolder = new GridPane();
        ToggleGroup group = new ToggleGroup();
        myShapeButtons = group;

        RadioButton square = new RadioButton();
        square.setUserData(SQUARE);
        square.setToggleGroup(group);
        square.setSelected(true);
        square.setOnAction(event -> shapeButton());
        buttonHolder.add(square, 0, 0);

        RadioButton diamond = new RadioButton(DIAMOND);
        diamond.setUserData(DIAMOND);
        diamond.setToggleGroup(group);
        diamond.setOnAction(event -> shapeButton());
        buttonHolder.add(diamond, 0, 1);

        RadioButton circle = new RadioButton(CIRCLE);
        circle.setUserData(CIRCLE);
        circle.setToggleGroup(group);
        circle.setOnAction(event -> shapeButton());
        buttonHolder.add(circle, 0, 2);

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
        Rectangle background = new Rectangle(size*2, size, Color.color(1.0, 1.0, 1.0));
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
        currDelayLeft = 0;
    }

    private void shapeButton() {
        myShape = myShapeButtons.getSelectedToggle().getUserData().toString();
    }

    private void loadButton() {
        isSimRunning = false;
        // topGrid.getChildren().remove(noCurrGrid);
        if (currGridDisplay != null)
            // topGrid.getChildren().remove(currentGridDisplay.getDisplay());
        clearMessage(myInfoBox);
        clearMessage(myStatsBox);
        try {
            XMLFilename = String.format(XML_FOLDER + myTextField.getText());
            generateGrids();
            // topGrid.add(currentGridDisplay.getDisplay(), 1, 1);
            scrollPane.setContent(currGridDisplay.getDisplay());
            addMessage(myInfoBox, START_SIM_MESSAGE);
        }
        catch (Exception e) {
            currGridDisplay = null;
            currGridStruct = null;
            // topGrid.getChildren().add(noCurrGrid);
            scrollPane.setContent(noCurrGrid);
            addMessage(myInfoBox, FILE_ERROR_MESSAGE);
        }
    }

    private void generateGrids() {
        currGridStruct = fileParser.generateGrid(XMLFilename, myShape);
        initDisplay(myShape, currGridStruct.getSize());
    }

    private void initDisplay(String myShape, int size) {
        currGridDisplay = new GridDisplay(myShape,size);
        for(int row = 0; row < size; row++){
            for(int col = 0; col < size; col++){
                Cell currCell = currGridStruct.getCellAtIndex(row,col);
                currGridDisplay.addCellToDisplay(row,col,currCell);
            }
        }
    }

    private void clearMessage (Pane messageBox) {
        Node message = new Text("");
        for (Node child: messageBox.getChildren()) {
            if (child instanceof Label) { //***
                message = child;
            }
        }
        messageBox.getChildren().remove(message);
    }
    private void addMessage (Pane messageBox, String message) {
        Label l = new Label(message);
        l.setFont(new Font("Menlo", FONT_SIZE / 2)); //***
        l.setWrapText(true);
        l.setMaxWidth(BOX_WIDTH-2);
        messageBox.getChildren().remove(1);
        messageBox.getChildren().add(l);
    }

    private void step () {
        if (currGridStruct != null && (isSimRunning || shouldStep)){
            if (currDelayLeft > 0){
                currDelayLeft--;
            } else {
                checkSlider();
                currGridStruct.step();
                if (shouldStep) {
                    shouldStep = false;
                } else {
                    currDelayLeft = simDelay;
                }
            }
        }
    }

    private void checkSlider() {
        double sliderValue = mySlider.getValue();
        int speed = discrete(sliderValue);
        simDelay = (int) (10 * Math.pow(2, speed));
    }

    private int discrete(double speed) {
        if (speed < -3.5) {
            return 4;
        } else if (speed < -2.5) {
            return 3;
        } else if (speed < -1.5) {
            return 2;
        } else if (speed < -0.5) {
            return 1;
        } else if (speed < 0.5) {
            return 0;
        } else if (speed < 1.5) {
            return -1;
        } else if (speed < 2.5) {
            return -2;
        } else if (speed < 3.5) {
            return -3;
        } else {
            return -4;
        }
    }

    public static void main(String[] args){
        launch(args);
    }
}
