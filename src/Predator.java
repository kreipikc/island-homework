import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Predator extends Animal {
    protected Map<String, Integer> preyProbability;

    public Predator(int x, int y) {
        super(x, y);
        preyProbability = new HashMap<>();
    }

    @Override
    void eat(List<Animal> animals, List<Plant> plants) {
        synchronized (animals) {  // Синхронизируем доступ к списку животных
            for (Animal prey : animals) {
                if (prey != this && prey.isAlive() && prey.x == this.x && prey.y == this.y) {
                    Integer probability = preyProbability.get(prey.name);
                    if (probability != null && ThreadLocalRandom.current().nextInt(100) < probability) {
                        prey.isAlive = false;
                        return;  // Съели одну жертву и выходим
                    }
                }
            }
        }
    }
}