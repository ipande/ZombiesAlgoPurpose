package src;

public class Edge
{
    int to;
    int from;
    int snow;    
    public Edge(int to, int from, int snow){
        this.to = to;
        this.from = from;
        this.snow = snow;
    }    
    public int getTo() {
        return to;
    }
    public int getFrom() {
        return from;
    }
    public int getSnow() {
        return snow;
    }
}
