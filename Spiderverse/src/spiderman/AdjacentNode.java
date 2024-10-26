package spiderman;
import java.util.ArrayList;
import java.util.List;

public class AdjacentNode {

    private Dimension data;
    private AdjacentNode next;
    private List<AdjacentNode> connections;

    public AdjacentNode(){
        data = null;
        next = null;
        connections = new ArrayList<>();
    }

    public AdjacentNode(Dimension data, AdjacentNode next){
        this.data = data;
        this.next = next;
    }

    public Dimension getData() {return data;}
    public AdjacentNode getNext() {return next;}
    
    public void setData(Dimension newData) {data = newData;}
    public void setNext(AdjacentNode newNext) {next = newNext;}
    public void addConnection(AdjacentNode node){
        if (node != null){
            connections.add(node);
        }
    }

}
