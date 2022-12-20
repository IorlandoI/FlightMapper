public class Path {
    public int cost = 0;
    public int time = 0;
    public String city = "";
    
    Path(String dest, int price, int time){
        city = dest;
        cost = price;
        this.time = time;
    }
}