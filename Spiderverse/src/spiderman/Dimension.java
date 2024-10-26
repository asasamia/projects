package spiderman;
import java.util.ArrayList;
import java.util.List;

public class Dimension {
    
    private int code;
    private int canonNumber;
    private int weight;
    private List<Person> people;

    public Dimension (int code, int canonNumber, int weight){
        this.code = code;
        this.canonNumber = canonNumber;
        this.weight = weight;
        this.people = new ArrayList<>();
    }

    public int getCode() {return code;}
    public int getCanonNum() {return canonNumber;}
    public int getWeight() { return weight;}
    public void addPerson (Person person){
        if (person != null){
            people.add(person);
        }
    }
    public List<Person> getPeople(){
        return people;
    }
}
