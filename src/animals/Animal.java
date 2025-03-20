package animals;

import plants.Plant;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Animal {
    protected String name;
    protected double weight;
    public int maxPerCell;
    public int maxSpeed;
    public double foodNeeded;
    protected int x, y;
    public boolean isAliveParam = true;
    public double hunger = 0.0;

    public Animal(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public abstract void eat(List<Animal> animals, List<Plant> plants);
    public abstract void move();
    public abstract Animal reproduce();

    public void updateHunger() {
        hunger += foodNeeded / 10;
        if (hunger >= foodNeeded * 10) {
            isAliveParam = false;
        }
        if (ThreadLocalRandom.current().nextInt(100) < 1) {
            isAliveParam = false;
        }
    }

    public boolean isAlive() { return isAliveParam; }
    public void SetIsAlive(boolean param) { isAliveParam = param; }
    public double getWeight() { return weight; }
    public int getX() { return x; }
    public int getY() { return y; }
    public String getName() { return name; }
}