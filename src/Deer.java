import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Deer extends Herbivore {
    public Deer(int x, int y) {
        super(x, y);
        name = "Deer";
        weight = 300.0;
        maxPerCell = 20;
        maxSpeed = 4;
        foodNeeded = 50.0;
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
        int dx = ThreadLocalRandom.current().nextInt(-maxSpeed, maxSpeed + 1);
        int dy = ThreadLocalRandom.current().nextInt(-maxSpeed, maxSpeed + 1);
        x = Math.max(0, Math.min(SimulationParams.WIDTH - 1, x + dx));
        y = Math.max(0, Math.min(SimulationParams.HEIGHT - 1, y + dy));
    }

    @Override
    Animal reproduce() {
        return new Deer(x, y);
    }
}
