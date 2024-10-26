package spiderman;
import java.util.*;

/**
 * Steps to implement this class main method:
 * 
 * Step 1:
 * DimensionInputFile name is passed through the command line as args[0]
 * Read from the DimensionsInputFile with the format:
 * 1. The first line with three numbers:
 *      i.    a (int): number of dimensions in the graph
 *      ii.   b (int): the initial size of the cluster table prior to rehashing
 *      iii.  c (double): the capacity(threshold) used to rehash the cluster table 
 * 2. a lines, each with:
 *      i.    The dimension number (int)
 *      ii.   The number of canon events for the dimension (int)
 *      iii.  The dimension weight (int)
 * 
 * Step 2:
 * SpiderverseInputFile name is passed through the command line as args[1]
 * Read from the SpiderverseInputFile with the format:
 * 1. d (int): number of people in the file
 * 2. d lines, each with:
 *      i.    The dimension they are currently at (int)
 *      ii.   The name of the person (String)
 *      iii.  The dimensional signature of the person (int)
 * 
 * Step 3:
 * ColliderOutputFile name is passed in through the command line as args[2]
 * Output to ColliderOutputFile with the format:
 * 1. e lines, each with a different dimension number, then listing
 *       all of the dimension numbers connected to that dimension (space separated)
 * 
 * @author Seth Kelley
 */

public class Collider {

    private static HashMap<Integer, List<Integer>> adjacencyList = new HashMap<>();
    private static HashMap<Integer, List<Person>> dimensionPeople = new HashMap<>();


    private static void createAdjacencyList(AdjacentNode[] clusters){
        for (AdjacentNode node : clusters){
            if (node != null){

                Dimension front = node.getData();
                int frontCode = front.getCode();
                adjacencyList.putIfAbsent(frontCode, new ArrayList<>());

                AdjacentNode next = node.getNext();
                while (next != null){
                    Dimension dimension = next.getData();
                    int code = dimension.getCode();

                    adjacencyList.putIfAbsent(code, new ArrayList<>());

                    if(!adjacencyList.get(frontCode).contains(code)){
                        adjacencyList.get(frontCode).add(code);
                    }
                    if (!adjacencyList.get(code).contains(frontCode)){
                        adjacencyList.get(code).add(frontCode);
                    }
                    next = next.getNext();
                }
            }
        }
    }

    private static void spiderverse(){
        int numPeople = StdIn.readInt();
        for (int i = 0; i < numPeople; i++){
            int current = StdIn.readInt();
            String name = StdIn.readString();
            int signature = StdIn.readInt();
            Person newPerson = new Person(current, name, signature);

            dimensionPeople.computeIfAbsent(current, k -> new ArrayList<>()).add(newPerson);
        }
    }

    private static void printList() {
        for (Map.Entry<Integer, List<Integer>> entry : adjacencyList.entrySet()) {
            StdOut.print(entry.getKey() + " ");
            for (Integer dimension : entry.getValue()) {
                StdOut.print(dimension + " ");
            }
            StdOut.println();
        }
    }

    public static HashMap<Integer, List<Integer>> getList(){
        return adjacencyList;
    }

    public static HashMap<Integer, List<Person>> getDimensionList(){
        return dimensionPeople;
    }

    public static void main(String[] args) {

        if ( args.length < 3 ) {
            StdOut.println(
                "Execute: java -cp bin spiderman.Collider <dimension INput file> <spiderverse INput file> <collider OUTput file>");
                return;
        }

        String dimensionFile = args[0];
        Clusters.main(new String[] {dimensionFile, "clusters.out"});
        AdjacentNode[] clusters = Clusters.getClusters();

        String spiderverseFile = args[1];
        StdIn.setFile(spiderverseFile);
        spiderverse();
        createAdjacencyList(clusters);

        String outputFile = args[2];
        StdOut.setFile(outputFile);
        printList();
    }
}