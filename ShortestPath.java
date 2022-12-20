import java.util.*;
import java.io.*;
public class ShortestPath{
    public LinkedList<LinkedList<Path>> travelRoutes = new LinkedList<LinkedList<Path>>();
    public LinkedList<LinkedList<PathInStack>> allPathsToDestination = new LinkedList<LinkedList<PathInStack>>();

    //This is our constructor for the class
    ShortestPath(){
        travelRoutes = FileOfDestinations();
     }


     /*This method reads in the file and constructs a linked list of linked list to determine
     *all possible paths between airlines(adjacency list)
     */
    public LinkedList<LinkedList<Path>> FileOfDestinations(){
        
        LinkedList<LinkedList<Path>> travelPaths = new LinkedList<LinkedList<Path>>();
        
        int numbOfEdges = 0;
        String name = "";


        int indDelim;
        int indNextDelim;
        String locationOne;
        String locationTwo;
        int positionOne = 0;
        int positionTwo = 0;
        int price = 0;
        int time = 0;
        String row = "";

        //This chunk of code takes a filename from the input and attempts to open it
        Scanner scnr = new Scanner(System.in);
        
        System.out.println("Insert file name: ");
        name = scnr.next();
        File pathInfo = new File(name);
        
        try{
            Scanner scan = new Scanner(pathInfo);
            /*This reads the first value from the file which should indicate
             *the number of edges
             */
            numbOfEdges = scan.nextInt();
            scan.nextLine();

            /*This for loop parses the information by row and then by the delimeter
             *so it can find the correct cost and Time. It also constructs the linked 
             *list so we know all posible paths
             */
            for(int i = 0; i < numbOfEdges; i++){

                Boolean locOneExists = false;
                Boolean locTwoExists = false;

                /*This chunk of code parses through the text in the file
                 * it seperates the locations, cost, and time 
                 */
                row = scan.nextLine();
                row = row.trim();
                indDelim = row.indexOf("|");
                indNextDelim = row.indexOf("|", indDelim + 1);
                locationOne = row.substring(0,indDelim);
                locationTwo = row.substring(indDelim + 1,indNextDelim);
                indDelim = indNextDelim;
                indNextDelim = row.indexOf("|", indNextDelim + 1);
                price = Integer.parseInt(row.substring(indDelim + 1, indNextDelim));
                time = Integer.parseInt(row.substring(indNextDelim + 1, row.length()));

                
                /*This for loop checks to see if the city already exists within
                 *the linked list by checking the first value of the linked list
                 *within the linked list
                 */
                for(int j = 0; j < travelPaths.size(); j++){
                    if(locationOne.equals(travelPaths.get(j).getFirst().city)){
                        locOneExists = true;
                        positionOne = j;
                    }
                    if(locationTwo.equals(travelPaths.get(j).getFirst().city)){
                        locTwoExists = true;
                        positionTwo = j;
                    }
                }

                /*This chunk of code verifies if the location already exists in the 
                 *travelRoutes linked list depending on the results of the loop. 
                 *If it already exists then it will retrieve the Linked list with
                 *the initial node containg the matching city and ir will insert
                 *a new path node with the appropriate city, price, and time
                 */
                if(locOneExists){
                    travelPaths.get(positionOne).addLast(new Path(locationTwo, price, time));
                } else{
                    travelPaths.addLast(new LinkedList<Path>());
                    travelPaths.getLast().addFirst(new Path(locationOne, 0, 0));
                    travelPaths.getLast().addLast(new Path(locationTwo, price, time));
                }

                /*This does what the above does except for the second node and adds
                 *the first location 
                 */
                if(locTwoExists){
                    travelPaths.get(positionTwo).addLast(new Path(locationOne, price, time));
                } else{
                    travelPaths.addLast(new LinkedList<Path>());
                    travelPaths.getLast().addFirst(new Path(locationTwo, 0, 0));
                    travelPaths.getLast().addLast(new Path(locationOne, price, time));
                }
            }    
        } catch(FileNotFoundException e){
            System.out.println("File not found");
        }
        return travelPaths;
    }




    public void DesiredPathFile(){
        int rows;
        int indDelim;
        int indNextDelim;
        String locationOne;
        String locationTwo;
        String row = "";
        String sortBy; 


        /*This chunk of code reads in the file name 
         *and makes a file variable 
         */
        Scanner fileName = new Scanner(System.in);
        System.out.println("Input file name containing cities and what to sort by: ");
        String Name = fileName.next();
        fileName.close();
        File desired = new File(Name);


        try{
            Scanner read = new Scanner(desired);
            rows = read.nextInt();
            read.nextLine();

            for(int i = 0; i < rows; i++){
                row = read.nextLine();
                row = row.trim();
                indDelim = row.indexOf("|");
                indNextDelim = row.indexOf("|", indDelim + 1);
                locationOne = row.substring(0,indDelim);
                locationTwo = row.substring(indDelim + 1,indNextDelim);
                sortBy = row.substring(indNextDelim + 1, row.length());
                

                getpaths(locationOne, locationTwo);
                int flightNum = i + 1;
                if(sortBy.equals("C")){
                    System.out.println("Flight " + flightNum + " " + locationOne + ", " + locationTwo + " (Cost)");
                    getMinCost();
                }else if(sortBy.equals("T")){
                    System.out.println("Flight " + flightNum + " " + locationOne + ", " + locationTwo + " (Time)");
                    getMinTime();
                }

                
            }
        } catch(FileNotFoundException e){
            System.out.println("File was not found");
        }

    }



    public void getMinCost(){
        int totalCost = 0;
        int totalTime = 0;

        LinkedList<PathInStack> particularPath = new LinkedList<PathInStack>();
        LinkedList<LinkedList<PathInStack>> optimalPaths = new LinkedList<LinkedList<PathInStack>>();
        optimalPaths.add(new LinkedList<PathInStack>());
        optimalPaths.add(new LinkedList<PathInStack>());
        optimalPaths.add(new LinkedList<PathInStack>());
        LinkedList<Integer> optimalCost = new LinkedList<Integer>();
        optimalCost.add(-1);
        optimalCost.add(-1);
        optimalCost.add(-1);
        LinkedList<Integer> time = new LinkedList<Integer>();

        //The First loop iterates through the possible paths
        for(int i = 0; i < allPathsToDestination.size(); i++){
                particularPath = allPathsToDestination.get(i);
                //The second loop iterates through the nodes of a particular path
                for(int j = 0; j < particularPath.size() - 1; j++){
                    //The third loop finds the location of origin point of the flight in the adjacency list
                    for(int k = 0; k < travelRoutes.size(); k++){
                        //The if statement verifies whether the origin point of the flight is found
                        if(travelRoutes.get(k).getFirst().city.equals(particularPath.get(j).city)){
                            //The last for loop iterates through the origin flight adjacemcy list to find the cost
                            for (int l = 0; l < travelRoutes.get(k).size(); l++){
                                if(travelRoutes.get(k).get(l).city.equals(particularPath.get(j + 1).city)){
                                    totalCost = totalCost + travelRoutes.get(k).get(l).cost;
                                    totalTime = totalTime + travelRoutes.get(k).get(l).time;
                                }
                            }
                        }
                    }

                }
                //These if else statements find the best three paths
                if(totalCost < optimalCost.get(0) || optimalCost.get(0) < 0){
                    optimalPaths.add(0, new LinkedList<PathInStack>(particularPath));
                    optimalCost.add(0, totalCost);
                    time.add(0, totalTime);
                }else if(totalCost < optimalCost.get(1) || optimalCost.get(1) < 0){
                    optimalPaths.add(1, new LinkedList<PathInStack>(particularPath));
                    optimalCost.add(1, totalCost);
                    time.add(1, totalTime);
                }else if(totalCost < optimalCost.get(2) || optimalCost.get(1) < 0){
                    optimalPaths.add(2, new LinkedList<PathInStack>(particularPath));
                    optimalCost.add(2, totalCost);
                    time.add(2, totalTime);
                }
                totalCost = 0;
                totalTime = 0;
        }

        for(int i = 0; i < 3; i++){
            if(0 < optimalPaths.get(i).size()){
                System.out.print("Path " + ++i + ": " );
                --i;
                for(int j = 0; j < optimalPaths.get(i).size();j++){
                    if(j + 1 < optimalPaths.get(i).size()){
                        System.out.print(optimalPaths.get(i).get(j).city + " -> ");
                    } else{
                        System.out.print(optimalPaths.get(i).get(j).city + ". Time: " + time.get(i) +" Cost: ");
                        System.out.printf("%.2f", (double)optimalCost.get(i));
                        System.out.println();
                    }
                }

            }
        }
        System.out.println();
    }


    

    public void getMinTime(){
        int totalCost = 0;
        int totalTime = 0;

        LinkedList<PathInStack> particularPath = new LinkedList<PathInStack>();
        LinkedList<LinkedList<PathInStack>> optimalPaths = new LinkedList<LinkedList<PathInStack>>();
        optimalPaths.add(new LinkedList<PathInStack>());
        optimalPaths.add(new LinkedList<PathInStack>());
        optimalPaths.add(new LinkedList<PathInStack>());
        LinkedList<Integer> optimalTime = new LinkedList<Integer>();
        optimalTime.add(-1);
        optimalTime.add(-1);
        optimalTime.add(-1);
        LinkedList<Integer> cost = new LinkedList<Integer>();

        //The First loop iterates through the possible paths
        for(int i = 0; i < allPathsToDestination.size(); i++){
                particularPath = allPathsToDestination.get(i);
                //The second loop iterates through the nodes of a particular path
                for(int j = 0; j < particularPath.size() - 1; j++){
                    //The third loop finds the location of origin point of the flight in the adjacency list
                    for(int k = 0; k < travelRoutes.size(); k++){
                        //The if statement verifies whether the origin point of the flight is found
                        if(travelRoutes.get(k).getFirst().city.equals(particularPath.get(j).city)){
                            //The last for loop iterates through the origin flight adjacemcy list to find the time
                            for (int l = 0; l < travelRoutes.get(k).size(); l++){
                                if(travelRoutes.get(k).get(l).city.equals(particularPath.get(j + 1).city)){
                                    totalCost = totalCost + travelRoutes.get(k).get(l).cost;
                                    totalTime = totalTime + travelRoutes.get(k).get(l).time;
                                }
                            }
                        }
                    }

                }
                //These if else statements find the best three paths
                if(totalTime < optimalTime.get(0) || optimalTime.get(0) < 0){
                    optimalPaths.add(0, new LinkedList<PathInStack>(particularPath));
                    cost.add(0, totalCost);
                    optimalTime.add(0, totalTime);
                }else if(totalTime < optimalTime.get(1) || optimalTime.get(1) < 0){
                    optimalPaths.add(1, new LinkedList<PathInStack>(particularPath));
                    cost.add(1, totalCost);
                    optimalTime.add(1, totalTime);
                }else if(totalTime < optimalTime.get(2) || optimalTime.get(1) < 0){
                    optimalPaths.add(2, new LinkedList<PathInStack>(particularPath));
                    cost.add(2, totalCost);
                    optimalTime.add(2, totalTime);
                }
                totalCost = 0;
                totalTime = 0;
        }

        for(int i = 0; i < 3; i++){
            if(0 < optimalPaths.get(i).size()){
                System.out.print("Path " + ++i + ": " );
                --i;
                for(int j = 0; j < optimalPaths.get(i).size();j++){
                    if(j + 1 < optimalPaths.get(i).size()){
                        System.out.print(optimalPaths.get(i).get(j).city + " -> ");
                    } else{
                        System.out.print(optimalPaths.get(i).get(j).city + ". Time: " + optimalTime.get(i) +" Cost: ");
                        System.out.printf("%.2f", (double)cost.get(i));
                        System.out.println();
                    }
                }

            }
        }
        System.out.println();
    }



    public void getpaths(String start, String end){
        LinkedList<PathInStack> currentPath = new LinkedList<PathInStack>();
        LinkedList<PathInStack> stack = new LinkedList<PathInStack>();
        LinkedList<LinkedList<PathInStack>> possiblePaths = new LinkedList<LinkedList<PathInStack>>();

        boolean toAdd = true;
        boolean endExists = false;
        int startRow = 0;
        int currentRow = 0;
        int StackInsertCounter = 0;

        if(start.equals(end)){
            System.out.println("The Originating city and the destination are the same so optimal path time and cost are 0");
            return;
        }

        /* This for loop finds where the starting point city
         * exists within the first linked list and
         * checks whether the city exists validation 
         * in the travelRoutes linked list
         */
        for(int i = 0; i < travelRoutes.size(); i++){
            if(travelRoutes.get(i).getFirst().city.equals(start)){
                currentPath.add(new PathInStack(travelRoutes.get(i).getFirst().city,0));
                startRow = i;
            }
            if(travelRoutes.get(i).getFirst().city.equals(end)){
                endExists = true;
            }
        }

        if(currentPath.size() == 0 || endExists == false){
            System.out.println("One of the cites of interes is not in the domain");
            return;
        }

        /*This for loop populates the stack with nodes connected to the
         * Starting point so the next loop conditions work
        */
        for(int i = 0; (i < (travelRoutes.get(startRow).size() - 1)); i++){
            stack.push(new PathInStack(travelRoutes.get(startRow).get(i+1).city, StackInsertCounter));
            StackInsertCounter++;
        }


        /* This loop will run until all the possible paths are found from
        *  From our starting point to our end point
        */
        while(stack.size() > 0){
            PathInStack currentNode = stack.pop();

            /* This for loop updates the current path By checking whether the
             * currently popped was pushed in the stack before the nodes in the 
             * current path and removes the nodes from the path if they were pushed
             * into the stack after the current node
             */
            for(int i = 1; i < currentPath.size(); i++){
                if(currentNode.stackPosition < currentPath.get(i).stackPosition){
                    int currentSize = currentPath.size() - i;
                    for(int j = 0; j < currentSize; j++)
                    currentPath.removeLast();
                }
            }

            /*This if statement checks to see if the destination node has been
             * reached. If it has it adds the last node to the currentPath and
             * then adds the path to the possiblePaths linked list
             */
            if(currentNode.city.equals(end)){
                currentPath.add(currentNode);
                possiblePaths.add(new LinkedList<PathInStack>(currentPath));
                currentPath.removeLast();
                continue;
            }

            currentPath.add(currentNode);

            /*This for loop finds where the city of the popped node is in
             *our adjacency List
            */
            for(int i = 0; i < travelRoutes.size(); i++){
                if(travelRoutes.get(i).getFirst().city.equals(currentNode.city)){
                    currentRow = i;
                }
            }

            /*These nested for loop add nodes to the stack depending on
             * whether the nodes are currently in the currentPath list
             */
            for(int i = 0; i < travelRoutes.get(currentRow).size();i++){
                for(int j = 0; j < currentPath.size(); j++){
                    if(travelRoutes.get(currentRow).get(i).city.equals(currentPath.get(j).city)){
                        toAdd = false;
                    }
                }
                if(toAdd){
                    stack.push(new PathInStack(travelRoutes.get(currentRow).get(i).city, StackInsertCounter));
                    StackInsertCounter++;
                }
                toAdd = true;

            }

        }

        allPathsToDestination = possiblePaths;
        
    }

}