package cellsociety;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class PredPreyCell extends Cell {
    public static final Paint SHARK_COLOR = Color.LIGHTSLATEGRAY;
    public static final Paint FISH_COLOR = Color.LIGHTSEAGREEN;
    public static final Paint EMPTY_COLOR = Color.SKYBLUE;

    private int fishReproduction;
    private int sharkReproduction;
    private int sharkDeathRate;
    private int lifeSpan = 0;
    private int sharkEnergy = sharkDeathRate;

    enum PredPreyCellState{
        SHARK, FISH, EMPTY
    }

    public PredPreyCell(double width, double height, String currState, String shape, double sharkFertility, double fishFertility, double sharkDeathRate){
        super(width,height,PredPreyCellState.valueOf(currState), shape);
        fishReproduction = (int)fishFertility;
        sharkReproduction = (int)sharkFertility;
        this.sharkDeathRate = (int)sharkDeathRate;
        changeDisplay();
    }

    public void calcNewState(ArrayList<HashSet<Cell>> emptySpaces){
        if(currState == PredPreyCellState.EMPTY && nextState == null) {
            nextState = PredPreyCellState.EMPTY;
            emptySpaces.get(1).add(this);
            lifeSpan = 0;
        }
        //fish
        else if(currState == PredPreyCellState.FISH) {
            Cell newSpace = selectMovement(emptySpaces);
            if(newSpace != null) {
                newSpace.setNextState(PredPreyCellState.FISH);
                //need to set newspace lifespan to current lifespan
                emptySpaces.get(1).remove(newSpace);
                if(lifeSpan >= fishReproduction) {
                    this.nextState = PredPreyCellState.FISH;
                    this.lifeSpan = 0;
                }
                else{
                    this.nextState = PredPreyCellState.EMPTY;
                    emptySpaces.get(1).add(this);
                }
            }
            else {
                nextState = PredPreyCellState.FISH;
                lifeSpan += 1;
            }
        }
        //shark
        else {
            if(sharkEnergy == 0) {
                nextState = PredPreyCellState.EMPTY;
                emptySpaces.get(1).add(this);
                lifeSpan = 0;
            }
        }
    }

    public void changeDisplay(){
        if(currState == PredPreyCellState.EMPTY) {
            visual.setFill(EMPTY_COLOR);
        }
        else if(currState == PredPreyCellState.SHARK) {
            visual.setFill(SHARK_COLOR);
        }
        else {
            visual.setFill(FISH_COLOR);
        }
    }

    private Cell selectMovement(ArrayList<HashSet<Cell>> emptySpaces){
        int index = 1;
        ArrayList<Cell> eligibleNeighbors = new ArrayList<Cell>();
        while(index < neighbors.length) {
            if(neighbors[index] != null &&
                    neighbors[index].getCurrState() == PredPreyCellState.EMPTY &&
                        emptySpaces.get(0).contains(neighbors[index])) {
                eligibleNeighbors.add(neighbors[index]);
            }
            index += 2;
        }
        if(eligibleNeighbors.size() == 0){
            return null;
        }
        else {
            return eligibleNeighbors.get(new Random().nextInt(eligibleNeighbors.size()));
        }
    }

}
