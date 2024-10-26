package spiderman;

public class Anomaly {
    private String name;
    private int homeDimension;
    private int dimensionalSignature;
    private int timeAllotted;

    public Anomaly(String name,int homeDimension, int dimensionalSignature, int timeAllotted) {
        this.name = name;
        this.homeDimension = homeDimension;
        this.dimensionalSignature = dimensionalSignature;
        this.timeAllotted = timeAllotted;
    }

    public String getName() {
        return name;
    }

    public int getHomeDimension() {
        return homeDimension;
    }

    public int getDimensionalSignature() {
        return dimensionalSignature;
    }

    public int getTimeAllotted() {
        return timeAllotted;
    }
}
