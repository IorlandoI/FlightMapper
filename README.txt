This project finds the shortest three paths using a search method to find all possible paths and then
calculates the the time and cost for all the paths possible it will then choose the best three paths depending 
on what the user specifies is a priority whether it be time or cost. Then the program will display
them to the user in the correct order. 
The Path class is used to create nodes with a city, cost, and time. This class is mainly used to 
produce an adjacency list so we can calculate the cost and time.
The PathInStack class has a city field and a position in Stack field the city field just lets the 
traversal know which node to visit next while the position in stack will tell the program when to 
backtrack and update the current path linked list.
The ShortestPath Class is made into an object and stores the Adjacency List. This class reads in 
the file containing the edges and nodes and builds the adjacency list. It also reads the file with
the requested flight optimizations through the DesiredPathFile method. The class displays the 
appropriate output partially through the DesiredPathFile and through the getMinTime method or the
getMinCost. The getpaths method finds all possible paths through the use of stacks implemented as 
linked lists and through the use of our adjacency list
The test2.txt is a file that goes with findtest2.txt that I used to test the program along with
test3.txt  and findtest3.txt
The Main class contains the main method and only creates a Shortest path object to allow for the creation
of the adjacency List.