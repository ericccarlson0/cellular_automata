package cellsociety.backend.gridstructures;

import cellsociety.Simulation;
import cellsociety.backend.Cell;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class PredPreyGrid extends GridStructure {
    private static final String GRID_TYPE_STRING = "PREDPREY_";
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

    public PredPreyGrid(int rowNum, int colNum, ArrayList<Double> percents, ArrayList<String> states, int numNeighbors, double sharkFertility, double fishFertility, double mortalityRate){
        super(rowNum,colNum,percents,states,numNeighbors);
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
            if(c.getCurrState() == Simulation.AllStates.PREDPREY_SHARK){
                speciesAt[0] = new Shark(deathRate,sharkFertility);
                speciesAt[1] = null;
                currSharks.add(c);
            }
            else if(c.getCurrState() == Simulation.AllStates.PREDPREY_FISH){
                speciesAt[0] = new Fish(fishFertility);
                speciesAt[1] = null;
                currFish.add(c);
            }
            else{
                speciesAt[0] = null;
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
        currSharks = nextSharks;
        currFish = nextFish;
        currEmpties = nextEmpties;
        nextSharks.clear();
        nextFish.clear();
        nextEmpties.clear();
    }

    private void emptyUpdate(Cell c) {

    }

    private void fishUpdate(Cell c) {

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
        speciesAtCell.get(c)[1] = null;
        c.setNextState(Simulation.AllStates.PREDPREY_EMPTY);
        nextEmpties.add(c);
    }

    private void setToShark(Cell c, Shark s){
        speciesAtCell.get(c)[1] = s;
        c.setNextState(Simulation.AllStates.PREDPREY_SHARK);
        nextSharks.add(c);
    }

    private void setToFish(Cell c, Fish f){
        speciesAtCell.get(c)[1] = f;
        c.setNextState(Simulation.AllStates.PREDPREY_FISH);
        nextFish.add(c);
    }

    private void moveShark(Cell c, Cell whereToMove) {
        Shark currShark = (Shark)speciesAtCell.get(c)[0];
        if(whereToMove.getCurrState() == Simulation.AllStates.PREDPREY_FISH){
            currShark.eat();
        }
        setToShark(whereToMove,currShark);
    }

    private Cell canMove(Cell c) {
        Cell whereToMove = null;
        List<Cell> nFish = new ArrayList<Cell>();
        List<Cell> nEmpties = new ArrayList<Cell>();
        List<Cell> neighbors = c.getNeighbors();
        for(Cell currNeighbor : neighbors){
            Simulation.AllStates typ = (Simulation.AllStates)currNeighbor.getCurrState();
            switch(typ){
                case PREDPREY_FISH:
                    nFish.add(currNeighbor);
                    break;
                case PREDPREY_EMPTY:
                    nEmpties.add(currNeighbor);
                    break;
            }
        }
        Simulation.AllStates cellType = (Simulation.AllStates)c.getCurrState();
        switch(cellType){
            case PREDPREY_SHARK:
                whereToMove = selectSharkSpot(nFish,nEmpties);
                break;
            case PREDPREY_FISH:
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
        if(speciesAtCell.get(whereToMove)[1] instanceof Shark){
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
        int nextLeftBeforeBabies;
        Fish(int babyRate){
            leftBeforeBabies = babyRate;
            nextLeftBeforeBabies = Integer.MAX_VALUE;
        }

        void decrease(){
            leftBeforeBabies--;
        }

        void updateValues(){
            leftBeforeBabies = nextLeftBeforeBabies;
            nextLeftBeforeBabies = Integer.MAX_VALUE;
        }

        void resetLeftBeforeBaby(int babyRate){
            nextLeftBeforeBabies = babyRate;
        }

        int leftBeforeBaby(){
            return leftBeforeBabies;
        }
    }
}
