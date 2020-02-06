#Refactoring Discussion
##Date: February 6, 2019

###1. Intra-file Duplication
 - We have been using hardcoded strings to choose which shape the cells will be displayed as. We will store these as strings instead, so they can be easily changed. This will not take long.
 
###2. Declarations should use Java collection interfaces such as "List" rather than specific implementation classes such as "LinkedList"
 - A pretty simple fix, should be easy to do in a couple of minutes.

###3. Magic numbers should not be used
 - We have multiple places in our code in which numbers are passed as arguments that are hard coded. We will fix this by making them variables instead. This will take a moderate amount of time, and is low priority.

###4. Unused "private" fields should be removed
 - We had an unused private variable in the grid display class. We removed it.

###5. Remove JavaFX from back end
 - We are currently storing JavaFX shape objects in the cell class. We need to remove all JavaFX calls from the back-end implementation so that we can keep the front end and the back end separate. This will be difficult to implement and is high priority to ensure that we are practicing the MVC structure. 

###6. Only one package
 - We only have one package for the entire source code. We will re-organize this to have multiple packages for each type of file. We will have a grid structures package, a front end package, and an XML package. This won't take too long but should be done soon.
 
###7. Protected 2d grid structure
 - Our 2d grid structure is a protected instance variable in the abstract GridStructure class. We did this so that the implementations of these classes can use the grid object. However, this instance variable should be private. To fix this, we could store the structure in each subclass rather than the abstract class. This would also allow us to be more flexible with the structure. 