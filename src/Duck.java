import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Duck extends Herbivore {
    public Duck(int x, int y) {
        super(x, y);
        name = "Duck";
        weight = 1.0;
        maxPerCell = 200;
        maxSpeed = 4;
        foodNeeded = 0.15;
    }

    @Override
    void eat(List<Animal> animals, List<Plant> plants) {
        synchronized (animals) {
            for (Animal prey : animals) {
                if (prey != this && prey.isAlive() && prey.x == this.x && prey.y == this.y && prey.name.equals("Caterpillar")) {
                    if (ThreadLocalRandom.current().nextInt(100) < 90) {  // 90% шанс съесть гусеницу
                        prey.isAlive = false;
                        hunger = 0.0;
                        return;
                    }
                }
            }
        }
        super.eat(animals, plants);  // Если нет гусениц, ест растения
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
        return new Duck(x, y);
    }
}