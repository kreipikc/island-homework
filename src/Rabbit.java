import java.util.concurrent.ThreadLocalRandom;

public class Rabbit extends Herbivore {
    public Rabbit(int x, int y) {
        super(x, y);
        name = "Rabbit";
        weight = 2.0;
        maxPerCell = 150;
        maxSpeed = 2;
        foodNeeded = 0.45;
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
        return new Rabbit(x, y);
    }
}