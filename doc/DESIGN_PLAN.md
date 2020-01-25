# **Simulation Design Plan**
### **Team Number**: 12 
### **Names**: 
Turner Jordan (tgj5),
Charles Papandreou (cnp20),
Eric Carlson (ecc45)

- **Introduction**:

This program will implement a closed framework that runs and animates various 2D simulations of Cellular Automata *(CA)*.
Use cases include *The Game of Life*, *Percolation*, *Segregation*, *Predator-Prey*, and *Fire*. 
We aim to design a program that can be extended to support many different CA simulations. This project will be
*open for extension* in that new cell objects can be created to accommodate CA simulations besides those described above, 
through the use of formatted XML documents. However, its *core functionality* -- the UI, the animation, and the simulation
 itself -- will be *closed* for modification. 

- **Overview**:

**Components**: The important classes are the *simulation* class, *grid* class, and *cell* class. The three are organized 
in a relatively linear manner -- the simulation class draws on the grid class, which draws on the cell class.

The **simulation** will have • a stage, • a scene, • a *grid* object corresponding to the current grid, and • two booleans
corresponding to whether or not the simulation has been run and whether or not a step has been completed. It will have
methods that • start the simulation, • stop the simulation, • go through a step, • render the stats display, and •
render the message box.

The **grid** will contain • a 2D array of cells and • a displayPane corresponding to that 2D array. It will have methods 
that • initialize the grid using the relevant information, • update the 2D array of cells, and • update and display the 
displayPane according to the 2D array of cells.

The **cell** object provides the *core functionality* of the project and is open to extension. Each type of CA simulation 
has a corresponding type of cell -- in our current implementation, these cell types are Subclasses. The abstract cell
class has instance variables for • the current state • the next state, and • the Shape object corresponding to the cell.
The *next state* is necessary so that all of the cells can be updated *at the same time*. It will have methods that •
calculate a cell's new state based on a list of its neighbors, • update  a cell's state based on what its new state has 
been set to, and • update the cell's Shape object according to its state.

**Two Instances of Flexibility**:

The **grid** class can use most implementations of a grid-- the instance variable that contains the matrix itself can be 
any sort of Collection, including • a 2D List and • a HashMap *(which would use %rowlen to index into rows)*.

The **cell** class is an abstract class so that methods such as **calcNewState** can be contingent on the cell type. In 
the case of certain implementations of Percolation, one would need to disregard the cells *beneath* a cell when deciding 
whether to turn an *empty* cell to a *full* cell. This would not be possible without an abstract class, as most CA 
simulations take into account all of a cell's neighbors.

- **User Interface**


- **Design Details**


- **Design Considerations**


- **Components**


- **Use Cases**


### **Team Responsibilities**

- **Team Member #1**: 

- **Team Member #2**: 

- **Team Member #3**: 

