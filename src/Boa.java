import java.util.concurrent.ThreadLocalRandom;

public class Boa extends Predator{
    public Boa(int x, int y) {
        super(x, y);
        name = "Boa";
        weight = 15.0;
        maxPerCell = 30;
        maxSpeed = 1;
        foodNeeded = 3.0;
        preyProbability.put("Fox", 15);
        preyProbability.put("Rabbit", 20);
        preyProbability.put("Mouse", 40);
        preyProbability.put("Duck", 10);
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
        return new Boa(x, y);
    }
}
