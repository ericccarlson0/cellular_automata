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
            sharkMove(c);
        }
        for(Cell c: currFish){
            fishMove(c);
        }
        for(Cell c: currEmpties){
            emptiesMove(c);
        }
        currSharks = nextSharks;
        currFish = nextFish;
        currEmpties = nextEmpties;
        nextSharks.clear();
        nextFish.clear();
        nextEmpties.clear();
    }

    private void emptiesMove(Cell c) {

    }

    private void fishMove(Cell c) {

    }

    private void sharkMove(Cell c) {
        ((Shark)speciesAtCell.get(c)[0]).decrease();
        Cell whereToMove = canMove(c);
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
        int nextEnergyLeft;
        int leftBeforeBabies;
        int nextLeftBeforeBabies;
        Shark(int deathRate,int babyRate){
            energyLeft = deathRate;
            leftBeforeBabies = babyRate;
            nextEnergyLeft = Integer.MAX_VALUE;
            nextLeftBeforeBabies = Integer.MAX_VALUE;
        }

        void updateValues(){
            energyLeft = nextEnergyLeft;
            leftBeforeBabies = nextLeftBeforeBabies;
            nextEnergyLeft = Integer.MAX_VALUE;
            nextLeftBeforeBabies = Integer.MAX_VALUE;
        }

        void decrease(){
            energyLeft--;
            leftBeforeBabies--;
        }

        void resetEnergy(int deathRate){
            nextEnergyLeft = deathRate;
        }

        void resetLeftBeforeBaby(int babyRate){
            nextLeftBeforeBabies = babyRate;
        }

        int leftBeforeBaby(){
            return leftBeforeBabies;
        }

        int energyLeft(){
            return energyLeft;
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
