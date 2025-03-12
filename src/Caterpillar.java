import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Caterpillar extends Herbivore {
    public Caterpillar(int x, int y) {
        super(x, y);
        name = "Caterpillar";
        weight = 0.01;
        maxPerCell = 1000;
        maxSpeed = 0;
        foodNeeded = 0.001;  // Уменьшили потребность в еде
    }

    @Override
    void eat(List<Animal> animals, List<Plant> plants) {
        synchronized (plants) {
            Iterator<Plant> iterator = plants.iterator();
            while (iterator.hasNext()) {
                Plant plant = iterator.next();
                if (plant.getX() == this.x && plant.getY() == this.y) {
                    iterator.remove();
                    hunger = 0.0;
                    return;
                }
            }
        }
        updateHunger();
    }

    @Override
    void move() {
        // Гусеницы не двигаются
    }

    @Override
    Animal reproduce() {
        return new Caterpillar(x, y);
    }
}