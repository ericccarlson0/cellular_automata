Charles Papandreou - cnp20
Turner Jordan - tgj5
Eric Carlson - ecc45

Player
 * int score
 * destructor currSelection
 
  * Collaborators:
    * simulation holds this object type
    * this object type uses game
 
 
Game
 * Methods:
    * Constructor
        * populates data structure to hold interactions by destructor type
        * populates data structure to hold allowed destructor types
    * compare types
        * takes in two destructors from simulation and then decides which wins, returning corresponding result
 * Collaborators:
    * player and simulation
    
Simulation
 * creates game
 * creates 2 players
 * allows game interaction calling player methods and game methods
  has player select type of destructor
 * based on type of destructors selected, calls game method to compare
 *based on winner increments score of correct player object
  