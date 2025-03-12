import java.util.*;
import java.util.concurrent.*;

public class Main {
    private List<Animal> animals = Collections.synchronizedList(new ArrayList<>());
    private List<Plant> plants = Collections.synchronizedList(new ArrayList<>());
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(3);
    private ExecutorService animalPool = Executors.newFixedThreadPool(10);
    private volatile boolean running = true;

    public Main() {
        initializeIsland();
    }

    private void initializeIsland() {
        Random rand = new Random();
        for (int i = 0; i < SimulationParams.INITIAL_WOLVES; i++) {
            animals.add(new Wolf(rand.nextInt(SimulationParams.WIDTH), rand.nextInt(SimulationParams.HEIGHT)));
        }
        for (int i = 0; i < SimulationParams.INITIAL_RABBITS; i++) {
            animals.add(new Rabbit(rand.nextInt(SimulationParams.WIDTH), rand.nextInt(SimulationParams.HEIGHT)));
        }
        for (int i = 0; i < SimulationParams.INITIAL_DUCKS; i++) {
            animals.add(new Duck(rand.nextInt(SimulationParams.WIDTH), rand.nextInt(SimulationParams.HEIGHT)));
        }
        for (int i = 0; i < SimulationParams.INITIAL_CATERPILLARS; i++) {
            animals.add(new Caterpillar(rand.nextInt(SimulationParams.WIDTH), rand.nextInt(SimulationParams.HEIGHT)));
        }
        for (int i = 0; i < SimulationParams.INITIAL_PLANTS; i++) {
            plants.add(new Plant(rand.nextInt(SimulationParams.WIDTH), rand.nextInt(SimulationParams.HEIGHT)));
        }
    }

    private void growPlants() {
        Random rand = new Random();
        synchronized (plants) {
            if (plants.size() < SimulationParams.MAX_TOTAL_PLANTS) {
                for (int i = 0; i < 3; i++) {  // Добавляем 3 растения каждые 2 секунды
                    plants.add(new Plant(rand.nextInt(SimulationParams.WIDTH), rand.nextInt(SimulationParams.HEIGHT)));
                }
            }
        }
    }

    private void animalLifeCycle() {
        List<Future<?>> futures = new ArrayList<>();
        List<Animal> animalsSnapshot = new ArrayList<>(animals);

        for (Animal animal : animalsSnapshot) {
            if (animal.isAlive()) {
                futures.add(animalPool.submit(() -> {
                    animal.eat(animals, plants);
                    animal.move();
                    synchronized (animals) {
                        Map<String, Integer> cellCount = new HashMap<>();
                        Map<String, Integer> totalCount = new HashMap<>();
                        animals.forEach(a -> {
                            if (a.isAlive()) {
                                totalCount.merge(a.name, 1, Integer::sum);
                                if (a.getX() == animal.getX() && a.getY() == animal.getY()) {
                                    cellCount.merge(a.name, 1, Integer::sum);
                                }
                            }
                        });
                        boolean canReproduce = false;
                        int reproduceChance = animal.name.equals("Wolf") ? 10 : 15;  // Травоядные размножаются чаще
                        if (animal.name.equals("Wolf")) {
                            canReproduce = cellCount.getOrDefault("Wolf", 0) < animal.maxPerCell && totalCount.getOrDefault("Wolf", 0) < SimulationParams.MAX_WOLVES;
                        } else if (animal.name.equals("Rabbit")) {
                            canReproduce = cellCount.getOrDefault("Rabbit", 0) < animal.maxPerCell && totalCount.getOrDefault("Rabbit", 0) < SimulationParams.MAX_RABBITS;
                        } else if (animal.name.equals("Duck")) {
                            canReproduce = cellCount.getOrDefault("Duck", 0) < animal.maxPerCell && totalCount.getOrDefault("Duck", 0) < SimulationParams.MAX_DUCKS;
                        } else if (animal.name.equals("Caterpillar")) {
                            canReproduce = cellCount.getOrDefault("Caterpillar", 0) < animal.maxPerCell && totalCount.getOrDefault("Caterpillar", 0) < SimulationParams.MAX_CATERPILLARS;
                        }
                        if (canReproduce && ThreadLocalRandom.current().nextInt(100) < reproduceChance) {
                            Animal offspring = animal.reproduce();
                            animals.add(offspring);
                        }
                    }
                }));
            }
        }

        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        synchronized (animals) {
            animals.removeIf(a -> !a.isAlive());
        }
    }

    private void printStats() {
        Map<String, Integer> animalCount = new HashMap<>();
        synchronized (animals) {
            animals.forEach(a -> animalCount.merge(a.name, 1, Integer::sum));
            if (animalCount.size() == 0) running = false;
        }
        System.out.println("Tick: Plants: " + plants.size() + ", Animals: " + animals.size());
        animalCount.forEach((name, count) -> System.out.println(name + ": " + count));
        System.out.println("------------------------");
    }

    public void start() {
        scheduler.scheduleAtFixedRate(this::growPlants, 0, 2, TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(this::animalLifeCycle, 0, 1, TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(this::printStats, 0, 1, TimeUnit.SECONDS);

        while (running) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        stop();
    }

    public void stop() {
        running = false;
        scheduler.shutdown();
        animalPool.shutdown();
    }

    public static void main(String[] args) {
        new Main().start();
    }
}