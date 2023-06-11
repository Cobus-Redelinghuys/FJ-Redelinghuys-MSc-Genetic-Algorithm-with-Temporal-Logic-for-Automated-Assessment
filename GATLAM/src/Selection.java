import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

abstract public class Selection {
    abstract SelectionResult selectChromosomes(HashSet<Chromosome> testSet, int generation);
}

class SelectionResult {
    final Chromosome[] winners;
    final Chromosome[] loosers;

    SelectionResult(Chromosome[] w, Chromosome[] l, StatsNode sn) {
        winners = w;
        loosers = l;
        statsNode = sn;
    }

    final StatsNode statsNode;
}

class RouletteSelection extends Selection {
    @Override
    SelectionResult selectChromosomes(HashSet<Chromosome> testSet, int generation) {
        Chromosome[] chromosomeArray = testSet.toArray(new Chromosome[0]);
        HashMap<Chromosome, InterpretorResults[]> results = Config.interpretor.run(chromosomeArray);
        HashMap<Chromosome, Float> fitnesses = new HashMap<>();
        for (Chromosome chromosome : chromosomeArray) {
            fitnesses.put(chromosome, Fitness.determineFitness(results.get(chromosome)));
        }

        float sum = 0;
        for (Float fal : fitnesses.values()) {
            sum += fal;
        }
        HashMap<Chromosome, Float> probabilities = new HashMap<>();
        for (Chromosome chromosome : chromosomeArray) {
            probabilities.put(chromosome, fitnesses.get(chromosome) / sum);
        }

        HashSet<Chromosome> winnersSet = new HashSet<>();
        HashSet<Chromosome> loosersSet = new HashSet<>();
        while (winnersSet.size() <= Config.tournamentSize) {
            int pos = Config.random.nextInt(probabilities.size());
            Chromosome candidate = probabilities.keySet().toArray(new Chromosome[0])[pos];
            if (Config.random.nextFloat() <= 1 - probabilities.get(candidate)) {
                winnersSet.add(candidate);
            }
        }

        while (winnersSet.size() <= Config.tournamentSize) {
            int pos = Config.random.nextInt(probabilities.size());
            Chromosome candidate = probabilities.keySet().toArray(new Chromosome[0])[pos];
            if (Config.random.nextFloat() <= probabilities.get(candidate)) {
                loosersSet.add(candidate);
            }
        }

        StatsNode sn = new StatsNode(generation, fitnesses);
        return new SelectionResult(winnersSet.toArray(new Chromosome[0]), loosersSet.toArray(new Chromosome[0]), sn);

    }
}

class StochasticUniversalSampling extends Selection {
    @Override
    SelectionResult selectChromosomes(HashSet<Chromosome> testSet, int generation) {
        Chromosome[] chromosomeArray = testSet.toArray(new Chromosome[0]);
        HashMap<Chromosome, InterpretorResults[]> results = Config.interpretor.run(chromosomeArray);
        HashMap<Chromosome, Float> fitnesses = new HashMap<>();
        for (Chromosome chromosome : chromosomeArray) {
            fitnesses.put(chromosome, Fitness.determineFitness(results.get(chromosome)));
        }

        HashSet<Chromosome> winnersSet = winners(fitnesses);
        HashSet<Chromosome> loosersSet = loosers(fitnesses);

        StatsNode sn = new StatsNode(generation, fitnesses);
        return new SelectionResult(winnersSet.toArray(new Chromosome[0]), loosersSet.toArray(new Chromosome[0]), sn);
    }

    HashSet<Chromosome> winners(HashMap<Chromosome, Float> fitnesses) {
        float sum = 0;
        for (Float fal : fitnesses.values()) {
            sum += 1 - fal;
        }

        float P = sum / Config.tournamentSize;
        float start = Config.random.nextFloat() * P;
        HashMap<Integer, Float> pointers = new HashMap<>();
        for (int i = 0; i < Config.tournamentSize; i++) {
            pointers.put(i, start + i * P);
        }

        HashSet<Chromosome> keep = new HashSet<>();

        float fitSum = fitnesses.get(fitnesses.keySet().toArray(new Chromosome[0])[0]);

        for (Float ptr : pointers.values()) {
            int i = 0;
            fitSum = 0;
            while (fitSum < ptr) {
                i++;
                fitSum += fitnesses.get(fitnesses.keySet().toArray(new Chromosome[0])[i]);
            }
            keep.add(fitnesses.keySet().toArray(new Chromosome[0])[i]);
        }

        return keep;
    }

    HashSet<Chromosome> loosers(HashMap<Chromosome, Float> fitnesses) {
        float sum = 0;
        for (Float fal : fitnesses.values()) {
            sum += fal;
        }

        float P = sum / Config.tournamentSize;
        float start = Config.random.nextFloat() * P;
        HashMap<Integer, Float> pointers = new HashMap<>();
        for (int i = 0; i < Config.tournamentSize; i++) {
            pointers.put(i, start + i * P);
        }

        HashSet<Chromosome> keep = new HashSet<>();

        float fitSum = fitnesses.get(fitnesses.keySet().toArray(new Chromosome[0])[0]);

        for (Float ptr : pointers.values()) {
            int i = 0;
            fitSum = 0;
            while (fitSum < ptr) {
                i++;
                fitSum += fitnesses.get(fitnesses.keySet().toArray(new Chromosome[0])[i]);
            }
            keep.add(fitnesses.keySet().toArray(new Chromosome[0])[i]);
        }

        return keep;

    }
}

class LinearRankSelection extends Selection {
    @Override
    SelectionResult selectChromosomes(HashSet<Chromosome> testSet, int generation) {
        Chromosome[] chromosomeArray = testSet.toArray(new Chromosome[0]);
        HashMap<Chromosome, InterpretorResults[]> results = Config.interpretor.run(chromosomeArray);
        HashMap<Chromosome, Float> fitnesses = new HashMap<>();
        HashMap<Float, ArrayList<Chromosome>> revFitness = new HashMap<>();
        for (Chromosome chromosome : chromosomeArray) {
            fitnesses.put(chromosome, Fitness.determineFitness(results.get(chromosome)));
            if (!revFitness.containsKey(fitnesses.get(chromosome))) {
                revFitness.put(fitnesses.get(chromosome), new ArrayList<>());
            }
            revFitness.get(fitnesses.get(chromosome)).add(chromosome);
        }

        Float[] sortedArray = fitnesses.values().toArray(new Float[0]);
        Arrays.sort(sortedArray);

        HashMap<Chromosome, Integer> randFitness = new HashMap<>();
        for (int i = 0; i < sortedArray.length; i++) {
            for (Chromosome chromosome : revFitness.get(sortedArray[i])) {
                randFitness.put(chromosome, i);
            }
        }

        Chromosome[] winners = winners(randFitness, testSet.size()).toArray(new Chromosome[0]);
        Chromosome[] loosers = loosers(randFitness, testSet.size()).toArray(new Chromosome[0]);

        StatsNode sn = new StatsNode(generation, fitnesses);
        return new SelectionResult(winners, loosers, sn);
    }

    HashSet<Chromosome> winners(HashMap<Chromosome, Integer> randFitness, int n) {
        HashSet<Chromosome> result = new HashSet<>();
        Chromosome[] chromosomes = randFitness.keySet().toArray(new Chromosome[0]);
        while (result.size() <= Config.tournamentSize) {
            int pos = Config.random.nextInt(chromosomes.length);
            float p = randFitness.get(chromosomes[pos]) / (float) (n * (n - 1));
            if (p < Config.random.nextFloat()) {
                result.add(chromosomes[pos]);
            }
        }
        return result;
    }

    HashSet<Chromosome> loosers(HashMap<Chromosome, Integer> randFitness, int n) {
        HashSet<Chromosome> result = new HashSet<>();
        Chromosome[] chromosomes = randFitness.keySet().toArray(new Chromosome[0]);
        while (result.size() <= Config.tournamentSize) {
            int pos = Config.random.nextInt(chromosomes.length);
            float p = randFitness.get(chromosomes[pos]) / (float) (n * (n - 1));
            if (1 - p < Config.random.nextFloat()) {
                result.add(chromosomes[pos]);
            }
        }
        return result;
    }
}