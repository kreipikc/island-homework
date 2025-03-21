package animals.herbivores;

import animals.Animal;
import animals.Herbivore;
import config.SimulationParams;
import plants.Plant;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Caterpillar extends Herbivore {
    public Caterpillar(int x, int y) {
        super(x, y);
        name = "Caterpillar";
        weight = 0.01;
        maxPerCell = 1000;
        maxSpeed = 1;
        foodNeeded = 0.001;
    }

    @Override
    public void eat(List<Animal> animals, List<Plant> plants) {
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
    public void move() {
        int dx = ThreadLocalRandom.current().nextInt(-maxSpeed, maxSpeed + 1);  // -1, 0 или 1
        int dy = ThreadLocalRandom.current().nextInt(-maxSpeed, maxSpeed + 1);  // -1, 0 или 1
        x = Math.max(0, Math.min(SimulationParams.WIDTH - 1, x + dx));
        y = Math.max(0, Math.min(SimulationParams.HEIGHT - 1, y + dy));
    }

    @Override
    public Animal reproduce() {
        return new Caterpillar(x, y);
    }
}