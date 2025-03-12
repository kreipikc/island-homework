import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Animal {
    protected String name;
    protected double weight;
    protected int maxPerCell;
    protected int maxSpeed;
    protected double foodNeeded;
    protected int x, y;
    protected boolean isAlive = true;
    protected double hunger = 0.0;

    public Animal(int x, int y) {
        this.x = x;
        this.y = y;
    }

    abstract void eat(List<Animal> animals, List<Plant> plants);
    abstract void move();
    abstract Animal reproduce();

    public void updateHunger() {
        hunger += foodNeeded / 10;  // Голод растет медленно (10 ходов до смерти)
        if (hunger >= foodNeeded * 10) {  // Умирает через 10 ходов без еды
            isAlive = false;
        }
        // Естественная смертность 1%
        if (ThreadLocalRandom.current().nextInt(100) < 1) {
            isAlive = false;
        }
    }

    public boolean isAlive() { return isAlive; }
    public double getWeight() { return weight; }
    public String getSymbol() { return name.substring(0, 1); }
    public int getX() { return x; }
    public int getY() { return y; }
    public String getName() { return name; }
}