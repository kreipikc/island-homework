import animals.Animal;
import animals.herbivores.*;
import animals.predators.*;
import config.SimulationParams;
import plants.Plant;

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
        for (int i = 0; i < SimulationParams.INITIAL_WOLVES; i++) animals.add(new Wolf(rand.nextInt(SimulationParams.WIDTH), rand.nextInt(SimulationParams.HEIGHT)));
        for (int i = 0; i < SimulationParams.INITIAL_RABBITS; i++) animals.add(new Rabbit(rand.nextInt(SimulationParams.WIDTH), rand.nextInt(SimulationParams.HEIGHT)));
        for (int i = 0; i < SimulationParams.INITIAL_DUCKS; i++) animals.add(new Duck(rand.nextInt(SimulationParams.WIDTH), rand.nextInt(SimulationParams.HEIGHT)));
        for (int i = 0; i < SimulationParams.INITIAL_CATERPILLARS; i++) animals.add(new Caterpillar(rand.nextInt(SimulationParams.WIDTH), rand.nextInt(SimulationParams.HEIGHT)));
        for (int i = 0; i < SimulationParams.INITIAL_BOAS; i++) animals.add(new Boa(rand.nextInt(SimulationParams.WIDTH), rand.nextInt(SimulationParams.HEIGHT)));
        for (int i = 0; i < SimulationParams.INITIAL_FOXES; i++) animals.add(new Fox(rand.nextInt(SimulationParams.WIDTH), rand.nextInt(SimulationParams.HEIGHT)));
        for (int i = 0; i < SimulationParams.INITIAL_BEARS; i++) animals.add(new Bear(rand.nextInt(SimulationParams.WIDTH), rand.nextInt(SimulationParams.HEIGHT)));
        for (int i = 0; i < SimulationParams.INITIAL_EAGLES; i++) animals.add(new Eagle(rand.nextInt(SimulationParams.WIDTH), rand.nextInt(SimulationParams.HEIGHT)));
        for (int i = 0; i < SimulationParams.INITIAL_HORSES; i++) animals.add(new Horse(rand.nextInt(SimulationParams.WIDTH), rand.nextInt(SimulationParams.HEIGHT)));
        for (int i = 0; i < SimulationParams.INITIAL_DEERS; i++) animals.add(new Deer(rand.nextInt(SimulationParams.WIDTH), rand.nextInt(SimulationParams.HEIGHT)));
        for (int i = 0; i < SimulationParams.INITIAL_MICE; i++) animals.add(new Mouse(rand.nextInt(SimulationParams.WIDTH), rand.nextInt(SimulationParams.HEIGHT)));
        for (int i = 0; i < SimulationParams.INITIAL_GOATS; i++) animals.add(new Goat(rand.nextInt(SimulationParams.WIDTH), rand.nextInt(SimulationParams.HEIGHT)));
        for (int i = 0; i < SimulationParams.INITIAL_SHEEP; i++) animals.add(new Sheep(rand.nextInt(SimulationParams.WIDTH), rand.nextInt(SimulationParams.HEIGHT)));
        for (int i = 0; i < SimulationParams.INITIAL_BOARS; i++) animals.add(new Boar(rand.nextInt(SimulationParams.WIDTH), rand.nextInt(SimulationParams.HEIGHT)));
        for (int i = 0; i < SimulationParams.INITIAL_BUFFALOS; i++) animals.add(new Buffalo(rand.nextInt(SimulationParams.WIDTH), rand.nextInt(SimulationParams.HEIGHT)));
        for (int i = 0; i < SimulationParams.INITIAL_PLANTS; i++) plants.add(new Plant(rand.nextInt(SimulationParams.WIDTH), rand.nextInt(SimulationParams.HEIGHT)));
    }

    private void growPlants() {
        Random rand = new Random();
        synchronized (plants) {
            if (plants.size() < SimulationParams.MAX_TOTAL_PLANTS) {
                for (int i = 0; i < 3; i++) {
                    plants.add(new Plant(rand.nextInt(SimulationParams.WIDTH), rand.nextInt(SimulationParams.HEIGHT)));
                }
            }
        }
    }

    private void animalLifeCycle() {
        List<Future<?>> futures = submitAnimalTasks();
        waitForTasksCompletion(futures);
        removeDeadAnimals();
    }

    private List<Future<?>> submitAnimalTasks() {
        List<Future<?>> futures = new ArrayList<>();
        List<Animal> animalsSnapshot = new ArrayList<>(animals);

        for (Animal animal : animalsSnapshot) {
            if (animal.isAlive()) {
                futures.add(animalPool.submit(() -> processAnimal(animal)));
            }
        }
        return futures;
    }

    private void processAnimal(Animal animal) {
        animal.eat(animals, plants);
        animal.move();
        tryReproduce(animal);
    }

    private void tryReproduce(Animal animal) {
        synchronized (animals) {
            Map<String, Integer> cellCount = countAnimalsInCell(animal.getX(), animal.getY());
            Map<String, Integer> totalCount = countTotalAnimals();
            boolean canReproduce = canAnimalReproduce(animal, cellCount, totalCount);
            int reproduceChance = getReproduceChance(animal);

            if (canReproduce && ThreadLocalRandom.current().nextInt(100) < reproduceChance) {
                Animal offspring = animal.reproduce();
                animals.add(offspring);
            }
        }
    }

    private Map<String, Integer> countAnimalsInCell(int x, int y) {
        Map<String, Integer> cellCount = new HashMap<>();
        animals.forEach(a -> {
            if (a.isAlive() && a.getX() == x && a.getY() == y) {
                cellCount.merge(a.getName(), 1, Integer::sum);
            }
        });
        return cellCount;
    }

    private Map<String, Integer> countTotalAnimals() {
        Map<String, Integer> totalCount = new HashMap<>();
        animals.forEach(a -> {
            if (a.isAlive()) {
                totalCount.merge(a.getName(), 1, Integer::sum);
            }
        });
        return totalCount;
    }

    private boolean canAnimalReproduce(Animal animal, Map<String, Integer> cellCount, Map<String, Integer> totalCount) {
        switch (animal.getName()) {
            case "animals.predators.Wolf": return cellCount.getOrDefault("animals.predators.Wolf", 0) < animal.maxPerCell && totalCount.getOrDefault("animals.predators.Wolf", 0) < SimulationParams.MAX_WOLVES;
            case "animals.herbivores.Rabbit": return cellCount.getOrDefault("animals.herbivores.Rabbit", 0) < animal.maxPerCell && totalCount.getOrDefault("animals.herbivores.Rabbit", 0) < SimulationParams.MAX_RABBITS;
            case "animals.herbivores.Duck": return cellCount.getOrDefault("animals.herbivores.Duck", 0) < animal.maxPerCell && totalCount.getOrDefault("animals.herbivores.Duck", 0) < SimulationParams.MAX_DUCKS;
            case "animals.herbivores.Caterpillar": return cellCount.getOrDefault("animals.herbivores.Caterpillar", 0) < animal.maxPerCell && totalCount.getOrDefault("animals.herbivores.Caterpillar", 0) < SimulationParams.MAX_CATERPILLARS;
            case "animals.predators.Boa": return cellCount.getOrDefault("animals.predators.Boa", 0) < animal.maxPerCell && totalCount.getOrDefault("animals.predators.Boa", 0) < SimulationParams.MAX_BOAS;
            case "animals.predators.Fox": return cellCount.getOrDefault("animals.predators.Fox", 0) < animal.maxPerCell && totalCount.getOrDefault("animals.predators.Fox", 0) < SimulationParams.MAX_FOXES;
            case "animals.predators.Bear": return cellCount.getOrDefault("animals.predators.Bear", 0) < animal.maxPerCell && totalCount.getOrDefault("animals.predators.Bear", 0) < SimulationParams.MAX_BEARS;
            case "animals.predators.Eagle": return cellCount.getOrDefault("animals.predators.Eagle", 0) < animal.maxPerCell && totalCount.getOrDefault("animals.predators.Eagle", 0) < SimulationParams.MAX_EAGLES;
            case "animals.herbivores.Horse": return cellCount.getOrDefault("animals.herbivores.Horse", 0) < animal.maxPerCell && totalCount.getOrDefault("animals.herbivores.Horse", 0) < SimulationParams.MAX_HORSES;
            case "animals.herbivores.Deer": return cellCount.getOrDefault("animals.herbivores.Deer", 0) < animal.maxPerCell && totalCount.getOrDefault("animals.herbivores.Deer", 0) < SimulationParams.MAX_DEERS;
            case "animals.herbivores.Mouse": return cellCount.getOrDefault("animals.herbivores.Mouse", 0) < animal.maxPerCell && totalCount.getOrDefault("animals.herbivores.Mouse", 0) < SimulationParams.MAX_MICE;
            case "animals.herbivores.Goat": return cellCount.getOrDefault("animals.herbivores.Goat", 0) < animal.maxPerCell && totalCount.getOrDefault("animals.herbivores.Goat", 0) < SimulationParams.MAX_GOATS;
            case "animals.herbivores.Sheep": return cellCount.getOrDefault("animals.herbivores.Sheep", 0) < animal.maxPerCell && totalCount.getOrDefault("animals.herbivores.Sheep", 0) < SimulationParams.MAX_SHEEP;
            case "animals.herbivores.Boar": return cellCount.getOrDefault("animals.herbivores.Boar", 0) < animal.maxPerCell && totalCount.getOrDefault("animals.herbivores.Boar", 0) < SimulationParams.MAX_BOARS;
            case "animals.herbivores.Buffalo": return cellCount.getOrDefault("animals.herbivores.Buffalo", 0) < animal.maxPerCell && totalCount.getOrDefault("animals.herbivores.Buffalo", 0) < SimulationParams.MAX_BUFFALOS;
            default: return false;  // На случай неизвестного вида
        }
    }

    private int getReproduceChance(Animal animal) {
        return animal.getName().equals("animals.predators.Wolf") || animal.getName().equals("animals.predators.Boa") ||
                animal.getName().equals("animals.predators.Fox") || animal.getName().equals("animals.predators.Bear") ||
                animal.getName().equals("animals.predators.Eagle") ? 10 : 15;
    }

    private void waitForTasksCompletion(List<Future<?>> futures) {
        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void removeDeadAnimals() {
        synchronized (animals) {
            animals.removeIf(a -> !a.isAlive());
        }
    }

    private void printStats() {
        Map<String, Integer> animalCount = new HashMap<>();
        synchronized (animals) {
            animals.forEach(a -> animalCount.merge(a.getName(), 1, Integer::sum));
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