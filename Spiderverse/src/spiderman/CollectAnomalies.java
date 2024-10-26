package spiderman;

import java.util.*;
import java.util.stream.Collectors;

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
 * HubInputFile name is passed through the command line as args[2]
 * Read from the HubInputFile with the format:
 * One integer
 *      i.    The dimensional number of the starting hub (int)
 * 
 * Step 4:
 * CollectedOutputFile name is passed in through the command line as args[3]
 * Output to CollectedOutputFile with the format:
 * 1. e Lines, listing the Name of the anomaly collected with the Spider who
 *    is at the same Dimension (if one exists, space separated) followed by 
 *    the Dimension number for each Dimension in the route (space separated)
 * 
 * @author Seth Kelley
 */

public class CollectAnomalies {

    private static HashMap<Integer, List<Integer>> adjacencyList = new HashMap<>();
    private static HashMap<Integer, List<Person>> dimensionPeople = new HashMap<>();

    private static void bfs(int startHub) {
        Queue<Integer> queue = new LinkedList<>();
        HashMap<Integer, Integer> parentMap = new HashMap<>();
        queue.add(startHub);
        HashSet<Integer> visited = new HashSet<>();
        visited.add(startHub);
    
        while (!queue.isEmpty()) {
            int current = queue.poll();
            for (int neighbor : adjacencyList.getOrDefault(current, new ArrayList<>())) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                    parentMap.put(neighbor, current);
                    if (dimensionPeople.containsKey(neighbor)) {
                        List<Person> peopleInDimension = dimensionPeople.get(neighbor);
                        boolean isAnomalyPresent = false;
                        boolean isSpiderPresent = false;
                        String anomalyName = "";
                        String spiderName = "";
                        for (Person person : peopleInDimension) {
                            if (person.getSignature() != neighbor) {
                                isAnomalyPresent = true;
                                anomalyName = person.getName();
                            } else if (person.getSignature() == neighbor) {
                                isSpiderPresent = true;
                                spiderName = person.getName();
                            }
                        }
                        if (isAnomalyPresent) {
                            List<Integer> path = reconstructPath(parentMap, neighbor, startHub);
                            if (path.get(0) == startHub) {
                                path.remove(0);
                            }
    
                            String routeOutput;
                            if (isSpiderPresent) {
                                Collections.reverse(path);
                                routeOutput = anomalyName + " " + spiderName + " " + pathToString(path) + " " + startHub;
                            } else {
                                List<Integer> returnPath = new ArrayList<>(path);
                                Collections.reverse(returnPath);
                                returnPath.remove(0);
                                routeOutput = anomalyName + " " + startHub + " " + pathToString(path) + " " + pathToString(returnPath) + " " + startHub;
                            }
                            StdOut.println(routeOutput);
                        }
                    }
                }
            }
        }
    }
    
    private static List<Integer> reconstructPath(HashMap<Integer, Integer> parentMap, int destination, int startHub) {
        LinkedList<Integer> path = new LinkedList<>();
        while (destination != startHub && parentMap.containsKey(destination)) {
            path.addFirst(destination);
            destination = parentMap.get(destination);
        }
        path.addFirst(startHub);
        return path;
    }
    
    private static String pathToString(List<Integer> path) {
        return path.stream()
                .map(Object::toString)
                .collect(Collectors.joining(" "));
    }
    
    public static void main(String[] args) {

        if ( args.length < 4 ) {
            StdOut.println(
                "Execute: java -cp bin spiderman.CollectAnomalies <dimension INput file> <spiderverse INput file> <hub INput file> <collected OUTput file>");
                return;
        }
        
        String dimensionFile = args[0];
        String spiderverseFile = args[1];

        String hubFile = args[2];
        StdIn.setFile(hubFile);
        int hubDim = StdIn.readInt();

        String outputFile = args[3];

        Collider.main(new String[] {dimensionFile, spiderverseFile, "collider.out"});
        adjacencyList = Collider.getList();
        dimensionPeople = Collider.getDimensionList();

        StdOut.setFile(outputFile);
        bfs(hubDim);
    }
}
