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
 * SpotInputFile name is passed through the command line as args[2]
 * Read from the SpotInputFile with the format:
 * Two integers (line seperated)
 *      i.    Line one: The starting dimension of Spot (int)
 *      ii.   Line two: The dimension Spot wants to go to (int)
 * 
 * Step 4:
 * TrackSpotOutputFile name is passed in through the command line as args[3]
 * Output to TrackSpotOutputFile with the format:
 * 1. One line, listing the dimenstional number of each dimension Spot has visited (space separated)
 * 
 * @author Seth Kelley
 */

public class TrackSpot {

    public static HashMap<Integer, List<Integer>> adjacencyList = new HashMap<>();

    private static boolean dfs(int current, int destination, List<Integer> route, Set<Integer> visited, Set<Integer> currentPath, HashMap<Integer, List<Integer>> adjacencyList) {
        
        if (!visited.contains(current)) {
            route.add(current);
            visited.add(current);
        }

        if (currentPath.contains(current)) {
            return false; 
        }
        currentPath.add(current);

        if (current == destination) {
            currentPath.remove(current);
            return true; 
        }

        for (int neighbor : adjacencyList.getOrDefault(current, new ArrayList<>())) {
            if (!visited.contains(neighbor) || !currentPath.contains(neighbor)) {
                if (dfs(neighbor, destination, route, visited, currentPath,adjacencyList)) {
                    currentPath.remove(current);
                    return true; 
                }
            }
        }
        
        currentPath.remove(current);
        return false;
    } 
    
    public static void outputRoute(List<Integer> path) {
        for (int dimension : path) {
            StdOut.print(dimension + " ");
        }
        StdOut.println();
    }
   
    
    public static void main(String[] args) {

        if ( args.length < 4 ) {
            StdOut.println(
                "Execute: java -cp bin spiderman.TrackSpot <dimension INput file> <spiderverse INput file> <spot INput file> <trackspot OUTput file>");
                return;
        }

        String dimensionFile = args[0];
        String spiderverseFile = args[1];
        String spotFile = args[2];
        String outputFile = args[3];

        Collider.main(new String[] {dimensionFile, spiderverseFile, "collider.out"});
        HashMap<Integer, List<Integer>> adjacencyList = Collider.getList();

        StdIn.setFile(spotFile);
        int startDimension = StdIn.readInt();
        int endDimension = StdIn.readInt();

        List<Integer> route = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();
        Set<Integer> currentPath = new HashSet<>();

        dfs(startDimension, endDimension, route, visited, currentPath, adjacencyList);
    
        StdOut.setFile(outputFile);
        outputRoute(route);    
    }
}
