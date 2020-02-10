# simulation -- team 12

This project implements a cellular automata simulator. Currently, the following simulation types are supported: Conways' Game of Life, Fire Spreading, Percolation, Segregation, Predator Prey, Rock Paper Scissors, and Ant Foraging. The simulations support different cell shapes, different numbers of neighbors, and the option to run a torodial simulation. 

Names: Charles Papandreou (cnp20), Eric Carlson (ecc45), Turner Jordan (tgj5)

### Timeline

Start Date: 01/23/2020

Finish Date: 02/10/2020

Hours Spent: 110

### Primary Roles
 - Charles Papandreou:  backend; separation of backend and frontend
 - Eric Carlson:        UI (SimulationMenu, SimulationUI, GridDisplay types)
 - Turner Jordan:       implementing simulation types


### Resources Used
#### Simulation Rules Sources:
 - Life: https://en.wikipedia.org/wiki/Conway's_Game_of_Life
 - Fire: https://www2.cs.duke.edu/courses/spring20/compsci308/assign/02_simulation/nifty/shiflet-fire/
 - Percolation: https://www2.cs.duke.edu/courses/spring20/compsci308/assign/02_simulation/PercolationCA.pdf
 - Rock Paper Scissors: https://softologyblog.wordpress.com/2018/03/23/rock-paper-scissors-cellular-automata/
 - Predator Prey: https://www2.cs.duke.edu/courses/spring20/compsci308/assign/02_simulation/nifty/scott-wator-world/
 - Segregation: https://www2.cs.duke.edu/courses/spring20/compsci308/assign/02_simulation/nifty/mccown-schelling-model-segregation/
 - Ant Foraging: https://cs.gmu.edu/~eclab/projects/mason/publications/alife04ant.pdf
 
#### Additional Sources: 
 - Duke University CS308 Labs, CS308 Lectures, CS308 TAs, and the CS308 Course Website (https://www2.cs.duke.edu/courses/spring20/compsci308/) were regularly consulted throughout the project.
 - The JavaFX API Documentation (https://docs.oracle.com/javafx/2/) and Java documentation (https://docs.oracle.com/en/java/) were used for most functionality questions.
 - Stack Overflow (https://stackoverflow.com/) was used to find fixes for other minor issues we came across.
 
### Running the Program
####Main class: 
Run the SimulationMenu.java class to launch the program. Load in an XML configuration file and select your desired presets on the on screen menu. The simulation will open in a new window. Multiple simulations can be run simultaneously. 

####Data files needed: 
Files needed are the xml specification files that are in data folder, and the TextElements.properties file in the resources folder to configure all text constants. SET THE DATA FOLDER AS RESOURCES ROOT. These include: *fire_test.xml*, *life_test.xml*, *percolation_test.xml*, *segregation_test.xml*, *wator_test.xml*, *rps_test.xml*, *ant_test.xml*. The following XML tags are used: 
 - SimType: indicates the simulation type the file is loading. Must be of type LIFE, FIRE, ANT, RPS, SEGREGATION, PERCOLATION, or PRED_PREY.
 - Size: sets both the row and width values for each simulation. All simulations are currently implemented as squares.
 - Percents: sets the probability distributions for each state during initialization, corresponding with the order of states in the following tag. These should be integer values that sum to 100, and each state should have a corresponding value.
 - States: the following states must be used for each simulation, in all caps, comma separated with no spaces in between:
    1. Life: ALIVE,EMPTY
    2. Fire: EMPTY,TREE,FIRE
    3. Ant: EMPTY,PHEROMONES,FOOD,NEST,FULL
    4. RPS: ROCK,PAPER,SCISSORS
    5. Segregation: EMPTY,ONE,TWO
    6. Percolation: BLOCK,EMPTY
    7. Predator Prey: EMPTY,SHARK,FISH
 - Misc: Any additional values that a specific simulation must require. These values should be comma separated with no spaces in between. The values for the implemented simulations are as follows:
    1. Life: NONE, misc field can be set to anything, will not affect simulation (it should still be there though)
    2. Fire: catchProb - the probability that a fire will catch a neighboring tree on fire
    3. Ant: newAntsPerStep - the number of new ants that are generated with each step, antLifeSpan: how many steps each ant can live for
    4. RPS: winThreshold - a double representing the percentage of games each player must lose before the state is changed
    5. Segregation: satisfactionThreshold - a double representing the percentage of neighbors that must be the same state for a cell to be satisfied (that is, not move)
    6. Percolation: initialFillProbability - the probability that a cell on the top row will be filled with water at the beginning of the simulation
    7. Predator Prey: sharkFertility - the rate at which sharks reproduce, fishFertility - the rate at which fish reproduce, mortalityRate - the rate at which organisms die


####Features implemented: 
In addition to the features described above, multiple simulations can be run simultaneously in different windows. Simulation speed can be adjusted using the speed slider. The simulation can be started, stopped, and individually stepped using buttons. The simulation can use the shapes triangles, hexagons, squares, circles, or diamonds, and can be run with the appropriate number of neighbors for each shape. The simulation can be run as a torus (continuous grid) or a flat grid.

### Notes/Assumptions

Assumptions or Simplifications: We have the user using XML files to generate the specification of each simulation, rather than having it input on the user interface. 

Interesting data files: rps_test.xml looks cool!

Known Bugs: None, but the data files must be formatted according to our specifications to be read. Otherwise, the application will reject them as invalid files. 

### Impressions
Our goal in the second leg of this project was to create an project that was interesting to the user and 
both extensible and easy to understand for the maintainer. A big part of the latter portion came down to 
separating backend from frontend and passing around data unnecessarily.
As far as the cellular automata themselves are concerned, the emergent behavior and visualization was 
fascinating. Our attempt to use this core concept in various ways throughout the project posed an
interesting problem of abstraction. On top of this, the use of different cell shapes added a second 
layer of difficulty. It was not obvious whether certain simulations would even operate meaningfully
for different cell shapes.
