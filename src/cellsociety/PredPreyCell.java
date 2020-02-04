package cellsociety;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class PredPreyCell extends Cell {
    public static final Paint SHARK_COLOR = Color.LIGHTSLATEGRAY;
    public static final Paint FISH_COLOR = Color.LIGHTCORAL;
    public static final Paint EMPTY_COLOR = Color.SEAGREEN;

    private int fishReproduction;
    private int sharkReproduction;
    private int sharkDeathRate;
    private int sharkEnergyLeft;
    private int sharkLeftBeforeBabies;
    private int fishLeftBeforeBabies;

    enum PredPreyCellState{
        SHARK, FISH, EMPTY
    }

    public PredPreyCell(double width, double height, String currState, String shape, double sharkFertility, double fishFertility, double deathRate){
        super(width,height,PredPreyCellState.valueOf(currState), shape);
        fishReproduction = (int)fishFertility;
        sharkReproduction = (int)sharkFertility;
        this.sharkDeathRate = (int)deathRate;
        initVals();
        changeDisplay();
    }

    private void initVals() {
        if(currState == PredPreyCellState.FISH){
            sharkLeftBeforeBabies = -1;
            sharkEnergyLeft = -1;
            fishLeftBeforeBabies = fishReproduction;
        }
        else if(currState == PredPreyCellState.SHARK){
            sharkLeftBeforeBabies = sharkReproduction;
            sharkEnergyLeft = sharkDeathRate;
            fishLeftBeforeBabies = -1;
        }
        else{
            sharkLeftBeforeBabies = -1;
            sharkEnergyLeft = -1;
            fishLeftBeforeBabies = -1;
        }
    }

    public void calcNewState(ArrayList<HashSet<Cell>> emptySpaces){
        if(currState == PredPreyCellState.EMPTY && nextState == null){
            nextState = PredPreyCellState.EMPTY;
            emptySpaces.get(1).add(this);
        }
        else if(currState == PredPreyCellState.FISH){
            adjustVals();
            Cell whereToMove = canMove();
        }
        else if(currState == PredPreyCellState.SHARK){

        }
    }

    private Cell canMove() {
        Cell whereToMove = null;
        int index = 1;
        ArrayList<Cell> sharks = new ArrayList<Cell>();
        ArrayList<Cell> fish = new ArrayList<Cell>();
        ArrayList<Cell> empties = new ArrayList<Cell>();
        while (index < neighbors.length){
            Cell currCellToCheck = neighbors[index];
            if(currCellToCheck != null){
                if(currCellToCheck.getCurrState() == PredPreyCellState.FISH){
                    fish.add(currCellToCheck);
                }
                else if(currCellToCheck.getCurrState() == PredPreyCellState.SHARK){
                    sharks.add(currCellToCheck);
                }
                else{
                    empties.add(currCellToCheck);
                }
            }
            index += 2;
        }
        whereToMove = selectSpotToMove(sharks,fish,empties);
        return whereToMove;
    }

    private Cell selectSpotToMove(ArrayList<Cell> sharks, ArrayList<Cell> fish, ArrayList<Cell> empties) {
        Cell whereToMove = null;
        if(currState == PredPreyCellState.SHARK){
            if(!fish.isEmpty()){
                whereToMove = fish.get(0);
            }
            else if(!empties.isEmpty()){
                whereToMove = empties.get(0);
            }
        }
        else if(currState == PredPreyCellState.FISH){
            if(!empties.isEmpty()){
                whereToMove = empties.get(0);
            }
        }
        return whereToMove;
    }

    private void adjustVals() {
        if(currState == PredPreyCellState.FISH){
            fishLeftBeforeBabies--;
        }
        else{
            sharkEnergyLeft--;
            sharkLeftBeforeBabies--;
        }
    }
//    public void calcNewState(ArrayList<HashSet<Cell>> emptySpaces){
//        if(currState == PredPreyCellState.EMPTY && nextState == null) {
//            nextState = PredPreyCellState.EMPTY;
//            emptySpaces.get(1).add(this);
//            lifeSpan = 0;
//        }
//        //fish
//        else if(currState == PredPreyCellState.FISH) {
//            Cell newSpace = selectMovement(emptySpaces);
//            if(newSpace != null) {
//                newSpace.setNextState(PredPreyCellState.FISH);
//                //need to set newspace lifespan to current lifespan
//                emptySpaces.get(1).remove(newSpace);
//                if(lifeSpan >= fishReproduction) {
//                    this.nextState = PredPreyCellState.FISH;
//                    this.lifeSpan = 0;
//                }
//                else{
//                    this.nextState = PredPreyCellState.EMPTY;
//                    emptySpaces.get(1).add(this);
//                }
//            }
//            else {
//                nextState = PredPreyCellState.FISH;
//                lifeSpan += 1;
//            }
//        }
//        //shark
//        else {
//            if(sharkEnergy == 0) {
//                nextState = PredPreyCellState.EMPTY;
//                emptySpaces.get(1).add(this);
//                lifeSpan = 0;
//            }
//        }
//    }

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

//    private Cell selectMovement(ArrayList<HashSet<Cell>> emptySpaces){
//        int index = 1;
//        ArrayList<Cell> eligibleNeighbors = new ArrayList<Cell>();
//        while(index < neighbors.length) {
//            if(neighbors[index] != null &&
//                    neighbors[index].getCurrState() == PredPreyCellState.EMPTY &&
//                        emptySpaces.get(0).contains(neighbors[index])) {
//                eligibleNeighbors.add(neighbors[index]);
//            }
//            index += 2;
//        }
//        if(eligibleNeighbors.size() == 0){
//            return null;
//        }
//        else {
//            return eligibleNeighbors.get(new Random().nextInt(eligibleNeighbors.size()));
//        }
//    }

}
