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
 * Neighborhood Types
 * Interactive Display Features

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

#### Features Affected by Assumptions


## New Features HowTo

#### Easy to Add Features

#### Other Features not yet Done

