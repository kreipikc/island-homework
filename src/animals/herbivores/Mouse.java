package animals.herbivores;

import animals.Animal;
import animals.Herbivore;
import config.SimulationParams;
import plants.Plant;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Mouse extends Herbivore {
    public Mouse(int x, int y) {
        super(x, y);
        name = "Mouse";
        weight = 0.05;
        maxPerCell = 500;
        maxSpeed = 1;
        foodNeeded = 0.01;
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
        int dx = ThreadLocalRandom.current().nextInt(-maxSpeed, maxSpeed + 1);
        int dy = ThreadLocalRandom.current().nextInt(-maxSpeed, maxSpeed + 1);
        x = Math.max(0, Math.min(SimulationParams.WIDTH - 1, x + dx));
        y = Math.max(0, Math.min(SimulationParams.HEIGHT - 1, y + dy));
    }

    @Override
    public Animal reproduce() {
        return new Mouse(x, y);
    }
}
