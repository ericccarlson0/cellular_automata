# Simulation Design Final
### Names

## Team Roles and Responsibilities

 * Team Member #1 - Charles Papandreou
    Refactoring Design, Simulation, GridStructure, Cell, GridDisplay, SimulationUI/Menu structure
    
 * Team Member #2 - Turner Jordan
    GridStructure Subclasses for Simulation Types

 * Team Member #3 - Eric Carlson
    Simulation UI, Simulation Menu, Frontend Display Subclasses


## Design goals

#### What Features are Easy to Add
 * Simulation Types
    * simply add a new gridStructure subclass, add state types to Simulation.AllStates, and 
 * Shapes
    * To add new cell shape types, create a new gridDisplay subclass.
    * this subclass will handle calculations for deciding how it displays
    * add to the method in simulation that takes in string type for creation of diff shaped displays
    * add in new button corresponding to shape in SimulationMenu
 * Neighborhood Types
    * Implement a new method in gridstructures to calculate which cells from structure get added to neighborhood
    * add method to switch case where neighborhoodType selects which getNeighbors function to call
 * Interactive Display Features
    * This would require adding some buttons in SimulationUI that change values that are passed into simulation.
    * wouldnt require structural changes, just adding of a few methods

## High-level Design

#### Core Classes
 * Cell
    * Holds necessary information for backend about cell states and list of neighbors
    * Handles updating of individual cell and its states
 * GridStructure (abstract)
    * Holds a structure of many cells
    * Backend for simulation
    * Extended for different simTypes, calculates new states for each cell differently depending on which subclass
 * GridDisplay (abstract)
    * Frontend for simulation display
    * Stores shapes that correspond to cells
    * Updates from simulation based on backend structure current states of cells
 * Simulation
    * Holds and GridStructure and GridDisplay
    * Handles initialization of frontend based on backend
    * Handles stepping and updating structure and display
    * Acts as top level controller between front end and back end of simulation
 * SimulationUI
    * New display window that holds simulations and some buttons
    * Allows starting stopping and stepping of individual simulations
    * Holds a simulation object that is stepped through depending on parameters of start stop and step
    * Can control speed through this window
 * SimulationMenu
    * Creates XML Parser Object
    * Displays Main Menu Interface
    * Allows selection of some parameters when creating a new Simulation
    * Controls Timeline
    * Generates new Simulation UI's
 * XMLParser
    * Parses a xml file to create GridStructure objects


## Assumptions that Affect the Design
 * Most assumptions are in the formatting of the XML file.
 * This creates random assortment of cells based on percentages of total grid
 * Specifies simulation specific parameters in here under a <misc> tag
 * Nice because allows all XML files to be the same format

#### Features Affected by Assumptions
 * Shapes and Neighborhood types cant be specified from XML files
 * No specific display layout can be specified - did this because it didnt seem to make sense to have to specify states manually for all cells in a 100x100 grid(10k cells)


## New Features HowTo

#### Easy to Add Features
 * Simulation Types
    * simply add a new gridStructure subclass, add state types to Simulation.AllStates, and 
 * Shapes
    * To add new cell shape types, create a new gridDisplay subclass.
    * this subclass will handle calculations for deciding how it displays
    * add to the method in simulation that takes in string type for creation of diff shaped displays
    * add in new button corresponding to shape in SimulationMenu
 * Neighborhood Types
    * Implement a new method in gridstructures to calculate which cells from structure get added to neighborhood
    * add method to switch case where neighborhoodType selects which getNeighbors function to call

#### Other Features not yet Done
 * Interactive Display Features
    * This would require adding some buttons in SimulationUI that change values that are passed into simulation.
    * wouldnt require structural changes, just adding of a few methods
 * Specifying certain cells to change by clicking
    * would require tracking of mouse clicks in SimulationUI, which would require passing of a parameter to Simulation to swap states
    * wouldnt require major structural changes, just adding of a few methods to the SimulationUI and Simulation classes.
 * Infinitely expanding grid
    * Would require changing structure to an arraylist and then adding a method to add new rows or columns to the structure depending on where expansion was needed.
    * Could get very slow on runtime eventually. Scrolling is already implemented to allow for this later though.

