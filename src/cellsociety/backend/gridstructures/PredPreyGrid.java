package cellsociety.backend.gridstructures;

import cellsociety.Simulation;
import cellsociety.backend.Cell;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class PredPreyGrid extends GridStructure {
    private static final String GRID_TYPE_STRING = "PRED_PREY_";
    private int deathRate;
    private int sharkFertility;
    private int fishFertility;

    private List<Cell> currSharks;
    private List<Cell> currFish;
    private List<Cell> currEmpties;

    private List<Cell> nextSharks;
    private List<Cell> nextFish;
    private List<Cell> nextEmpties;

    private HashMap<Cell,Object[]> speciesAtCell;

    public PredPreyGrid(int rowNum, int colNum, ArrayList<Double> percents, ArrayList<String> states, boolean isTorus, int numNeighbors, double sharkFertility, double fishFertility, double mortalityRate){
        super(rowNum,colNum,percents,states,numNeighbors, isTorus);
        this.deathRate = (int)mortalityRate;
        this.sharkFertility = (int)sharkFertility;
        this.fishFertility = (int)fishFertility;
        init();
        populateSpecies();
    }

    private void populateSpecies() {
        initStructures();
        for(Cell c: cellList){
            Object[] speciesAt = new Object[2];
            if(c.getCurrState() == Simulation.AllStates.PRED_PREY_SHARK){
                speciesAt[0] = new Shark(deathRate,sharkFertility);
                speciesAt[1] = null;
                currSharks.add(c);
            }
            else if(c.getCurrState() == Simulation.AllStates.PRED_PREY_FISH){
                speciesAt[0] = new Fish(fishFertility);
                speciesAt[1] = null;
                currFish.add(c);
            }
            else{
                speciesAt[0] = new Empty();
                speciesAt[1] = null;
                currEmpties.add(c);
            }
            speciesAtCell.put(c,speciesAt);
        }
    }

    private void initStructures() {
        speciesAtCell = new HashMap<>();
        currSharks = new ArrayList<>();
        currFish = new ArrayList<>();
        currEmpties = new ArrayList<>();
        nextSharks = new ArrayList<>();
        nextFish = new ArrayList<>();
        nextEmpties = new ArrayList<>();
    }

    @Override
    protected void calcNewStates() {
        for(Cell c: currSharks){
            sharkUpdate(c);
        }
        for(Cell c: currFish){
            fishUpdate(c);
        }
        for(Cell c: currEmpties){
            emptyUpdate(c);
        }
        updateStructures();
    }

    private void updateStructures() {
        currSharks = (ArrayList<Cell>)((ArrayList<Cell>)nextSharks).clone();
        currFish = (ArrayList<Cell>)((ArrayList<Cell>)nextFish).clone();
        currEmpties = (ArrayList<Cell>)((ArrayList<Cell>)nextEmpties).clone();
        nextSharks.clear();
        nextFish.clear();
        nextEmpties.clear();
        for(Cell c: cellList){
            speciesAtCell.get(c)[0] = speciesAtCell.get(c)[1];
            speciesAtCell.get(c)[1] = null;
        }
    }

    private void emptyUpdate(Cell c) {
        if(!(speciesAtCell.get(c)[1] instanceof Fish) && !(speciesAtCell.get(c)[1] instanceof Shark))
            setToEmpty(c);
    }

    private void fishUpdate(Cell c) {
        Fish currFish = (Fish)(speciesAtCell.get(c)[0]);
        currFish.decrease();
        Cell whereToMove = canMove(c);
        boolean eatenNow = speciesAtCell.get(c)[1] instanceof Shark;
        if(whereToMove != null) {
            if (!eatenNow) {
                if (currFish.readyForBaby()){
                    currFish.haveBaby();
                    setToFish(c, new Fish(fishFertility));
                }
                else
                    setToEmpty(c);
                boolean eatenNext = speciesAtCell.get(whereToMove)[1] instanceof Shark;
                if(eatenNext){
                    ((Shark)speciesAtCell.get(whereToMove)[1]).eat();
                }
                else
                    setToFish(whereToMove,currFish);
            }
        }
        else if(!eatenNow){
            setToFish(c,currFish);
        }
    }

    private void sharkUpdate(Cell c) {
        Shark currShark = (Shark)speciesAtCell.get(c)[0];
        currShark.decrease();
        Cell whereToMove = canMove(c);
        if(!currShark.shouldDie()){
            if(whereToMove != null){
                moveShark(c,whereToMove);
                if(currShark.readyForBaby()){
                    currShark.haveBaby();
                    setToShark(c,new Shark(deathRate,sharkFertility));
                }
                else
                    setToEmpty(c);
            }
            else
                setToShark(c,currShark);
        }
        else
            setToEmpty(c);
    }

    private void setToEmpty(Cell c) {
        speciesAtCell.get(c)[1] = new Empty();
        c.setNextState(Simulation.AllStates.PRED_PREY_EMPTY);
        nextEmpties.add(c);
    }

    private void setToShark(Cell c, Shark s){
        speciesAtCell.get(c)[1] = s;
        c.setNextState(Simulation.AllStates.PRED_PREY_SHARK);
        nextSharks.add(c);
    }

    private void setToFish(Cell c, Fish f){
        speciesAtCell.get(c)[1] = f;
        c.setNextState(Simulation.AllStates.PRED_PREY_FISH);
        nextFish.add(c);
    }

    private void moveShark(Cell c, Cell whereToMove) {
        Shark currShark = (Shark)speciesAtCell.get(c)[0];
        if(whereToMove.getCurrState() == Simulation.AllStates.PRED_PREY_FISH){
            currShark.eat();
        }
        setToShark(whereToMove,currShark);
    }

    private Cell canMove(Cell c) {
        Cell whereToMove = null;
        List<Cell> nFish = new ArrayList<>();
        List<Cell> nEmpties = new ArrayList<>();
        List<Cell> neighbors = c.getNeighbors();
        for(Cell currNeighbor : neighbors){
            Simulation.AllStates typ = (Simulation.AllStates)currNeighbor.getCurrState();
            switch(typ){
                case PRED_PREY_FISH:
                    nFish.add(currNeighbor);
                    break;
                case PRED_PREY_EMPTY:
                    nEmpties.add(currNeighbor);
                    break;
            }
        }
        Simulation.AllStates cellType = (Simulation.AllStates)c.getCurrState();
        switch(cellType){
            case PRED_PREY_SHARK:
                whereToMove = selectSharkSpot(nFish,nEmpties);
                break;
            case PRED_PREY_FISH:
                whereToMove = selectFishSpot(nEmpties);
                break;
        }
        return whereToMove;
    }

    private Cell selectFishSpot(List<Cell> nEmpties) {
        Cell whereToMove = null;
        if(!nEmpties.isEmpty()){
            int ind = (int)(Math.random() * nEmpties.size());
            whereToMove = nEmpties.remove(ind);
            if(speciesAtCell.get(whereToMove)[1] instanceof Fish){
                return selectFishSpot(nEmpties);
            }
        }
        return whereToMove;
    }

    private Cell selectSharkSpot(List<Cell> nFish, List<Cell> nEmpties) {
        Cell whereToMove = null;
        if(!nFish.isEmpty()){
            int ind = (int)(Math.random() * nFish.size());
            whereToMove = nFish.remove(ind);
        }
        else if(!nEmpties.isEmpty()){
            int ind = (int)(Math.random() * nEmpties.size());
            whereToMove = nEmpties.remove(ind);
        }
        if(whereToMove != null && speciesAtCell.get(whereToMove)[1] instanceof Shark){
            return selectSharkSpot(nFish,nEmpties);
        }
        return whereToMove;
    }

    @Override
    protected Cell createCell(int row, int col) {
        Simulation.AllStates selectedState = Simulation.AllStates.valueOf(GRID_TYPE_STRING+generateState());
        return new Cell(selectedState);
    }

    class Shark{
        int energyLeft;
        int leftBeforeBabies;
        int initEnergy;
        int initBabies;
        Shark(int deathRate,int babyRate){
            energyLeft = deathRate;
            leftBeforeBabies = babyRate;
            initEnergy = deathRate;
            initBabies = babyRate;
        }

        void decrease(){
            energyLeft--;
            leftBeforeBabies--;
        }

        void eat(){
            energyLeft = initEnergy;
        }

        void haveBaby(){
            leftBeforeBabies = initBabies;
        }

        boolean readyForBaby(){
            return leftBeforeBabies <= 0;
        }

        boolean shouldDie(){
            return energyLeft <= 0;
        }
    }

    class Fish{
        int leftBeforeBabies;
        int initBabies;
        Fish(int babyRate){
            leftBeforeBabies = babyRate;
            initBabies = babyRate;
        }

        void decrease(){
            leftBeforeBabies--;
        }

        boolean readyForBaby(){
            return leftBeforeBabies <= 0;
        }

        void haveBaby(){
            leftBeforeBabies = initBabies;
        }
    }

    class Empty{
        Empty(){

        }
    }
}
