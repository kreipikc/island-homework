package animals.predators;

import animals.Animal;
import animals.Predator;
import config.SimulationParams;
import plants.Plant;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Eagle extends Predator {
    public Eagle(int x, int y) {
        super(x, y);
        name = "Eagle";
        weight = 6.0;
        maxPerCell = 20;
        maxSpeed = 3;
        foodNeeded = 1.0;
        preyProbability.put("Fox", 10);
        preyProbability.put("Rabbit", 90);
        preyProbability.put("Mouse", 90);
        preyProbability.put("Duck", 80);
    }

    @Override
    public void eat(List<Animal> animals, List<Plant> plants) {
        super.eat(animals, plants); // Используем реализацию из Predator
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
        return new Eagle(x, y);
    }
}
