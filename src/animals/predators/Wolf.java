package animals.predators;

import animals.Animal;
import animals.Predator;
import config.SimulationParams;

import java.util.concurrent.ThreadLocalRandom;

public class Wolf extends Predator {
    public Wolf(int x, int y) {
        super(x, y);
        name = "Wolf";
        weight = 50.0;
        maxPerCell = 30;
        maxSpeed = 3;
        foodNeeded = 8.0;
        preyProbability.put("Horse", 10);
        preyProbability.put("Deer", 15);
        preyProbability.put("Rabbit", 60);
        preyProbability.put("Mouse", 80);
        preyProbability.put("Goat", 60);
        preyProbability.put("Sheep", 70);
        preyProbability.put("Boar", 15);
        preyProbability.put("Buffalo", 10);
        preyProbability.put("Duck", 40);
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
        return new Wolf(x, y);
    }
}