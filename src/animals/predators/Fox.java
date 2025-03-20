package animals.predators;

import animals.Animal;
import animals.Predator;
import config.SimulationParams;

import java.util.concurrent.ThreadLocalRandom;

public class Fox extends Predator {
    public Fox(int x, int y) {
        super(x, y);
        name = "Fox";
        weight = 8.0;
        maxPerCell = 30;
        maxSpeed = 2;
        foodNeeded = 2.0;
        preyProbability.put("Rabbit", 70);
        preyProbability.put("Mouse", 90);
        preyProbability.put("Duck", 60);
        preyProbability.put("Caterpillar", 40);
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
        return new Fox(x, y);
    }
}
