package spiderman;

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
 * 
 * Step 2:
 * ClusterOutputFile name is passed in through the command line as args[1]
 * Output to ClusterOutputFile with the format:
 * 1. n lines, listing all of the dimension numbers connected to 
 *    that dimension in order (space separated)
 *    n is the size of the cluster table.
 * 
 * @author Seth Kelley
 */

public class Clusters {

    private static AdjacentNode[] clusters;
    private static int clusterSize;

    public Clusters(){
        clusters = null;
        clusterSize = 0;
    }

    private static double total() {

        double dimensionsAdded = 0;

        for (int i = 0; i < clusterSize; i++){
            if (clusters[i] != null){
                AdjacentNode ptr = clusters[i];

                while (ptr != null){
                    dimensionsAdded++;
                    ptr = ptr.getNext();
                }
            }
        }

        return dimensionsAdded;
    }

    private static void rehash(){
        int newSize = clusterSize * 2;
        AdjacentNode[] newClusters = new AdjacentNode[newSize];

        for (int i = 0; i < clusterSize; i++) {
            AdjacentNode current = clusters[i];

            while (current != null) {

                Dimension dimension = current.getData();
                int newIndex = dimension.getCode() % newSize;
                AdjacentNode New = new AdjacentNode(dimension, newClusters[newIndex]);
                
                if (newClusters[newIndex] == null){
                    newClusters[newIndex] = New;
                }

                else {
                    New.setNext(newClusters[newIndex]);
                    newClusters[newIndex] = New;
                }  
                current = current.getNext();  
            }   
                
        }
        clusters = newClusters;
        clusterSize = newSize;
    }

    private static void connect() {

        int index1 = 0;
        int index2 = 0;

        for (int i = 0; i < clusterSize; i++) {
            
            if (i == 0) {
                index1 = clusterSize - 1;
                index2 = clusterSize - 2;
            }
            else if (i == 1){
                index1 = 0;
                index2 = clusterSize - 1;
            }
            else {
                index1 = i - 1;
                index2 = i - 2;
            }

            AdjacentNode temp1 = new AdjacentNode(clusters[index1].getData(), null);
            AdjacentNode temp2 = new AdjacentNode(clusters[index2].getData(), null);
            temp1.setNext(temp2);


            AdjacentNode ptr = clusters[i];
            while (ptr.getNext() != null){
                ptr = ptr.getNext();
            }
            ptr.setNext(temp1);
        }
    }

    public static AdjacentNode[] dimensions(String inputFile){
        StdIn.setFile(inputFile);
        int a = StdIn.readInt();
        int b = StdIn.readInt();
        double c = StdIn.readDouble();
        StdIn.readLine();

        clusters = new AdjacentNode[b];
        clusterSize = b;

        for (int i = 0; i < a; i++){
            int code = StdIn.readInt();
            int canonNumber = StdIn.readInt();
            int weight = StdIn.readInt();

            Dimension newDim = new Dimension(code, canonNumber, weight);
            AdjacentNode newNode = new AdjacentNode(newDim, null);


            if (total() >= c){
                rehash();
                c = c*2;
            }    
            int index = code % clusterSize;
            if (clusters[index] == null){
                clusters[index] = newNode;
            }
            else {
                newNode.setNext(clusters[index]);
                clusters[index] = newNode;
            }    
        }
        return clusters;
    }

    public static AdjacentNode[] getClusters(){
        return clusters;
    }
    public static void main(String[] args) {

        if ( args.length < 2 ) {
            StdOut.println(
                "Execute: java -cp bin spiderman.Clusters <dimension INput file> <collider OUTput file>");
                return;
        }
        
        String inputFile = args[0];
        clusters = dimensions(inputFile);

        String outputFile = args[1];
        StdOut.setFile(outputFile);
        connect();

        for (int i = 0; i < clusterSize; i++){
            if (clusters[i] != null){
                AdjacentNode ptr = clusters[i];
                while (ptr != null){
                    int code = ptr.getData().getCode();
                    StdOut.print(code + " ");
                    ptr = ptr.getNext();
                }
                StdOut.println();
            }
        }

        
        
    }
}
