package cellsociety.backend.gridstructures;

import cellsociety.Simulation;
import cellsociety.backend.Cell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AntGrid extends GridStructure{
    public static final String GRID_TYPE_STRING = "ANT_";
    private List<Ant> allAnts;
    private HashMap<Cell, List<Ant>> antsInCells;
    private Cell antNest;
    private int newAntsPerStep = 2;
    private int antLifeSpan = 500;

    public AntGrid(int rowNum, int colNum, List<Double> percents, List<String> states, boolean isTorus, int neighborhoodType, double Something) {
        super(rowNum, colNum, percents, states, neighborhoodType, isTorus);
        this.init();
        initializeAntMap();
        this.antNest = getCellAtIndex(20, 20);
        this.allAnts = new ArrayList<Ant>();
    }

    @Override
    protected void calcNewStates() {
        updateAnts();
        for(Cell c: cellList) {
            updateAntCell(c);
        }
    }

    @Override
    protected Cell createCell(int row, int col) {
        Simulation.AllStates state;
        if(row == 20 && col == 20) {
            state = Simulation.AllStates.ANT_NEST;
        }
        else if(row == 70 && col == 70) {
            state = Simulation.AllStates.ANT_FOOD;
        }
        else {
            state = Simulation.AllStates.ANT_EMPTY;
        }
        return new Cell(state);
    }

    protected void updateAntCell(Cell currCell) {
        currCell.setNextState(currCell.getCurrState());
    }

    protected void updateAnts() {
        for(int i = 0; i < newAntsPerStep; i++) {
            allAnts.add(new Ant(antLifeSpan));
        }
        for(Ant currAnt: allAnts) {
            if(currAnt.hasFood) {
                antReturnToNest(currAnt);
            }
            else {
                antFindFoodSource(currAnt);
            }
        }
    }

    protected void initializeAntMap() {
        antsInCells = new HashMap<Cell, List<Ant>>();
        for(Cell c: cellList){
            antsInCells.put(c, new ArrayList<Ant>());
        }
    }

    protected void antReturnToNest(Ant currAnt) {

    }

    protected void antFindFoodSource(Ant currAnt) {
        if(currAnt.currCell == antNest) {
            currAnt.orientation = getMaxFoodPherNeighbor(currAnt);
        }
    }

    protected int getMaxFoodPherNeighbor(Ant currAnt) {
        List<Cell> allNeighbors = currAnt.currCell.getNeighbors();
        double maxPher = -1.0;
        int index = 0;
        for(Cell currNeighbor: allNeighbors) {

        }
        return 1;
    }

    protected int getMaxHomePherNeighbor(Ant currAnt) {
        return 1;
    }

    class Ant{
        private int lifeSpan;
        private boolean hasFood;
        private Cell currCell;
        private Cell nextCell;
        private int orientation;

        public Ant(int lifespan) {
            this.lifeSpan = lifespan;
            this.hasFood = false;
            this.currCell = antNest;
            antsInCells.get(antNest).add(this);
        }
    }

}
