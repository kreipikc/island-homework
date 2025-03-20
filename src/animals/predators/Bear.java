package animals.predators;

import animals.Animal;
import animals.Predator;
import config.SimulationParams;

import java.util.concurrent.ThreadLocalRandom;

public class Bear extends Predator {
    public Bear(int x, int y) {
        super(x, y);
        name = "Bear";
        weight = 500.0;
        maxPerCell = 5;
        maxSpeed = 2;
        foodNeeded = 80.0;
        preyProbability.put("Boa", 80);
        preyProbability.put("Horse", 40);
        preyProbability.put("Deer", 80);
        preyProbability.put("Rabbit", 80);
        preyProbability.put("Mouse", 90);
        preyProbability.put("Goat", 70);
        preyProbability.put("Sheep", 70);
        preyProbability.put("Boar", 50);
        preyProbability.put("Buffalo", 20);
        preyProbability.put("Duck", 10);
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
        return new Bear(x, y);
    }
}
