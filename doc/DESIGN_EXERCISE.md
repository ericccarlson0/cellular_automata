Charles Papandreou - cnp20
Turner Jordan - tgj5
Eric Carlson - ecc45

1. How does a Cell know about its neighbors? How can it update itself without effecting its neighbors update?
 * Each cell object will hold 8 cell object pointers for its neighbors that will be used to determine updated state
 * Each cell will hold its current state and next state
 * update function will pass through all cells twice, calculating next state and then setting next state to curr state
 
2. What relationship exists between a Cell and a simulation's rules?
 * Cell will be an abstract class and each new type of simulation rules will correspond with a new cell subclass that has the necessary updating functionality for new simulation type
 
3. What is the grid? Does it have any behaviors? Who needs to know about it?
 * Grid class
 * holds cell objects
 * behaviors will be update to iterate over all cells and first calc then update state
 * only class that needs to know about the grid is the simulation_runner class
 
4. What information about a simulation needs to be the configuration file?
 * grid size
 * simulation type
 * title/author
 * initial state
 * list of other necessary values depending on simulation type
 
5. How is the graphical view of the simulation updated after all the cells have been updated?
 * iterate over the grid in display class and change colors of those that were updated on last pass.