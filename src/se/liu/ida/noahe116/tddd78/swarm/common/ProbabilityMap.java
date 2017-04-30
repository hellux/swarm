package se.liu.ida.noahe116.tddd78.swarm.common;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class ProbabilityMap<T> {
    public static final Random RAND = new Random();
    
    private List<T> items = new ArrayList<>();
    private List<Double> probabilities = new ArrayList<>();
    private double totalSum = 0;

    public ProbabilityMap<T> put(T item, double probability) {
        if (probability < 0) {
            throw new IllegalArgumentException("probability can't be negative: " + probability);
        }
        int index = this.items.indexOf(item);

        if (index == -1) {
            this.items.add(item);
            this.probabilities.add(probability);
        } else {
            this.probabilities.set(index, probability);
            this.totalSum -= this.probabilities.get(index);
        }

        this.totalSum += probability;
        return this;
    }

    public ProbabilityMap<T> put(ProbabilityMap<T> map) {
        for (int i = 0; i < map.items.size(); i++) {
            this.put(map.items.get(i), 
                     map.probabilities.get(i));
        }
        return this;
    }

    public T get() {
        return this.get(RAND);
    }

    public T get(Random rand) {
        double index = this.totalSum * rand.nextDouble();
        double cumulativeSum = 0;

        for (int i = 0; i < this.items.size(); i++) {
            cumulativeSum += this.probabilities.get(i);
            if (index < cumulativeSum) {
                return this.items.get(i);
            }
        }
        
        return null;
    }
    
}
