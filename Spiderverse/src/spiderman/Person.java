package spiderman;
import java.util.ArrayList;

public class Person {

    private int dimension;
    private String name;
    private int signature;
    private ArrayList<Integer> connectedDimensions;

    public Person(int dimension, String name, int dimensionalSignature) {
        this.dimension = dimension;
        this.name = name;
        this.signature = dimensionalSignature;
        this.connectedDimensions = new ArrayList<>();
    }

    public int getDimension() {
        return dimension;
    }

    public String getName() {
        return name;
    }

    public int getSignature() {
        return signature;
    }

    public void addConnectedDimension(int dimension) {
        connectedDimensions.add(dimension);
    }

    public ArrayList<Integer> getConnectedDimensions() {
        return connectedDimensions;
    }
    
    public String toString() {
        return "Dimension: " + dimension + ", Name: " + name + ", Dimensional Signature: " + signature;
    }

}
