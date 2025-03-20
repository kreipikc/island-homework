package animals;

import plants.Plant;

import java.util.Iterator;
import java.util.List;

public abstract class Herbivore extends Animal {
    public Herbivore(int x, int y) {
        super(x, y);
    }

    @Override
    public void eat(List<Animal> animals, List<Plant> plants) {
        synchronized (plants) {
            Iterator<Plant> iterator = plants.iterator();
            while (iterator.hasNext()) {
                Plant plant = iterator.next();
                if (plant.getX() == this.x && plant.getY() == this.y) {
                    iterator.remove();
                    hunger = 0.0;  // Сброс голода
                    return;
                }
            }
        }
        updateHunger();  // Если не поел, голод увеличивается
    }
}