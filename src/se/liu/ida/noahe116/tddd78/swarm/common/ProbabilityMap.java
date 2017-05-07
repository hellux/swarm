package se.liu.ida.noahe116.tddd78.swarm.common;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

/**
 * List of items with probabilities mapped to them.
 **/
public class ProbabilityMap<T> {
    private static final Random RAND = new Random();
    
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
    
    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (this.getClass() != o.getClass()) return false;
        ProbabilityMap<?> pm = (ProbabilityMap) o;
        if (this.totalSum!= pm.totalSum) return false;
        if (!this.items.equals(pm.items)) return false;
        if (!this.probabilities.equals(pm.probabilities)) return false;
        
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 1;

        hash = hash*281 + Double.hashCode(this.totalSum);
        hash = hash*97 + this.items.hashCode();
        hash = hash*89 + this.probabilities.hashCode();

        return hash;
    }
}
