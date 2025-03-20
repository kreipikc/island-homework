package plants;

public class Plant {
    private int x, y;
    private double weight = 1.0;

    public Plant(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public double getWeight() { return weight; }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}