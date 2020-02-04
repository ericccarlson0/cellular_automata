package cellsociety;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import javax.swing.*;

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
    private int nextSharkEnergyLeft;
    private int nextSharkLeftBeforeBabies;
    private int nextFishLeftBeforeBabies;

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
        }
        else if(currState == PredPreyCellState.FISH && nextState != PredPreyCellState.SHARK){
            adjustVals();
            PredPreyCell whereToMove = (PredPreyCell) canMove();
            if(whereToMove != null){
                moveCell(whereToMove);
            }
            else{
                if(nextState != PredPreyCellState.SHARK)
                    nextState = currState;
                    setNextVals(fishLeftBeforeBabies,sharkLeftBeforeBabies,sharkEnergyLeft);
            }
        }
        else if(currState == PredPreyCellState.SHARK){
            adjustVals();
            PredPreyCell whereToMove = (PredPreyCell) canMove();
            if(whereToMove != null){
                moveCell(whereToMove);
            }
            else{
                if(sharkEnergyLeft == 0){
                    nextState = PredPreyCellState.EMPTY;
                    setNextVals(-1,-1,-1);
                }
                else{
                    nextState = currState;
                    setNextVals(fishLeftBeforeBabies,sharkLeftBeforeBabies,sharkEnergyLeft);
                }
            }
        }
    }

    private void moveCell(PredPreyCell whereToMove) {
        if(currState == PredPreyCellState.FISH){
            if(whereToMove.getNextState() != PredPreyCellState.SHARK){
                if(fishLeftBeforeBabies == 0)
                    whereToMove.setNextVals(fishReproduction,sharkLeftBeforeBabies,sharkEnergyLeft);
                else
                    whereToMove.setNextVals(fishLeftBeforeBabies,sharkLeftBeforeBabies,sharkEnergyLeft);
                whereToMove.setNextState(PredPreyCellState.FISH);
            }
            if(fishLeftBeforeBabies == 0 && nextState != PredPreyCellState.SHARK){
                nextState = PredPreyCellState.FISH;
                setNextVals(fishReproduction,-1,-1);
            }
            else if(nextState != PredPreyCellState.SHARK){
                nextState = PredPreyCellState.EMPTY;
                setNextVals(-1,-1,-1);
            }
            if(nextState == PredPreyCellState.SHARK){
                nextSharkEnergyLeft = sharkDeathRate;
            }
            if(whereToMove.getNextState() == PredPreyCellState.SHARK)
                whereToMove.setNextVals(-1,whereToMove.getNextSharkBabies(),sharkDeathRate);
        }
        else if(currState == PredPreyCellState.SHARK){
            if(sharkEnergyLeft != 0){
                boolean ateFish = false;
                if(whereToMove.getNextState()==PredPreyCellState.FISH)
                    ateFish = true;
                whereToMove.setNextState(PredPreyCellState.SHARK);
                if(sharkLeftBeforeBabies == 0){
                    nextState = PredPreyCellState.SHARK;
                    setNextVals(-1,sharkReproduction,sharkDeathRate);
                    if(ateFish)
                        whereToMove.setNextVals(fishLeftBeforeBabies,sharkReproduction,sharkDeathRate);
                    else
                        whereToMove.setNextVals(fishLeftBeforeBabies,sharkReproduction,sharkEnergyLeft);
                }
                else{
                    nextState = PredPreyCellState.EMPTY;
                    setNextVals(-1,-1,-1);
                    if(ateFish)
                        whereToMove.setNextVals(fishLeftBeforeBabies,sharkLeftBeforeBabies,sharkDeathRate);
                    else
                        whereToMove.setNextVals(fishLeftBeforeBabies,sharkLeftBeforeBabies,sharkEnergyLeft);
                }
            }
            else{
                nextState = PredPreyCellState.EMPTY;
                setNextVals(-1,-1,-1);
            }
        }
    }

    private int getNextSharkBabies() {
        return nextSharkLeftBeforeBabies;
    }

    private void setNextVals(int fBabies, int sBabies, int eLeft){
        nextFishLeftBeforeBabies = fBabies;
        nextSharkLeftBeforeBabies = sBabies;
        nextSharkEnergyLeft = eLeft;
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
                int ind = (int)Math.random() * fish.size();
                whereToMove = fish.remove(ind);
                if(whereToMove.getNextState() == PredPreyCellState.SHARK){
                    return selectSpotToMove(sharks,fish,empties);
                }
            }
            else if(!empties.isEmpty()){
                int ind = (int)Math.random() * empties.size();
                whereToMove = empties.remove(ind);
                if(whereToMove.getNextState() == PredPreyCellState.SHARK){
                    return selectSpotToMove(sharks,fish,empties);
                }
            }
        }
        else if(currState == PredPreyCellState.FISH){
            if(!empties.isEmpty()){
                int ind = (int)Math.random() * empties.size();
                whereToMove = empties.remove(ind);
                if(whereToMove.getNextState() == PredPreyCellState.FISH){
                    return selectSpotToMove(sharks,fish,empties);
                }
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

    @Override
    public void updateState(){
        currState = nextState;
        nextState = null;
        sharkEnergyLeft = nextSharkEnergyLeft;
        sharkLeftBeforeBabies = nextSharkLeftBeforeBabies;
        fishLeftBeforeBabies = nextFishLeftBeforeBabies;
        nextFishLeftBeforeBabies = -1;
        nextSharkEnergyLeft = -1;
        nextSharkLeftBeforeBabies = -1;
    }

}
