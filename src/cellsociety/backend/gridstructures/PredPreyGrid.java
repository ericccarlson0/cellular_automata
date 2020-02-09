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

    private HashMap<Cell,List<List<Integer>>> cellValues;

    public PredPreyGrid(int size, ArrayList<Double> percents, ArrayList<String> states, int numNeighbors, double sharkFertility, double fishFertility, double mortalityRate){
        super(size,percents,states,numNeighbors);
        this.deathRate = (int)mortalityRate;
        this.sharkFertility = (int)sharkFertility;
        this.fishFertility = (int)fishFertility;
        currSharks = new ArrayList<>();
        currFish = new ArrayList<>();
        currEmpties = new ArrayList<>();
        nextSharks = new ArrayList<>();
        nextFish = new ArrayList<>();
        nextEmpties = new ArrayList<>();
        init();
        populateSpecies();
    }

    private void populateSpecies() {
        cellValues = new HashMap<>();
        ArrayList<Integer> next = new ArrayList<>(Arrays.asList(Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE));
        for (Cell c: cellList){
            if(c.getCurrState() == Simulation.AllStates.PREDPREY_FISH){
                ArrayList<Integer> curr = new ArrayList<>(Arrays.asList(fishFertility,Integer.MAX_VALUE,Integer.MAX_VALUE));
                currFish.add(c);
                cellValues.put(c,new ArrayList<>(Arrays.asList(curr,next)));
            }
            else if(c.getCurrState() == Simulation.AllStates.PREDPREY_SHARK){
                ArrayList<Integer> curr = new ArrayList<>(Arrays.asList(Integer.MAX_VALUE,sharkFertility,deathRate));
                currSharks.add(c);
                cellValues.put(c,new ArrayList<>(Arrays.asList(curr,next)));
            }
            else{
                ArrayList<Integer> curr = new ArrayList<>(Arrays.asList(Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE));
                currEmpties.add(c);
                cellValues.put(c,new ArrayList<>(Arrays.asList(curr,next)));
            }
        }
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

    }

    @Override
    protected Cell makeCellOfType(int row, int col) {
        Simulation.AllStates selectedState = Simulation.AllStates.valueOf(GRID_TYPE_STRING+generateState());
        return new Cell(selectedState);
    }
}
