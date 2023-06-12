import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

abstract public class Selection {
    abstract SelectionResult selectChromosomes(HashSet<Chromosome> population, int generation);

    static Selection getSelection(String selectionMethod){
        if(selectionMethod.equals("RouletteSelection")){
            return new RouletteSelection();
        }
        if(selectionMethod.equals("StochasticUniversalSampling")){
            return new StochasticUniversalSampling();
        }
        if(selectionMethod.equals("LinearRankSelection")){
            return new LinearRankSelection();
        }
        if(selectionMethod.equals("ExponentialRankSelection")){
            return new ExponentialRankSelection();
        }
        if(selectionMethod.equals("TournamentSelection")){
            return new TournamentSelection();
        }
        if(selectionMethod.equals("TruncationSelection")){
            return new TruncationSelection();
        }
        throw new RuntimeException("Invalid Selection Method");
    }
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
    SelectionResult selectChromosomes(HashSet<Chromosome> population, int generation) {
        Chromosome[] chromosomeArray = population.toArray(new Chromosome[0]);
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
    SelectionResult selectChromosomes(HashSet<Chromosome> population, int generation) {
        Chromosome[] chromosomeArray = population.toArray(new Chromosome[0]);
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
    SelectionResult selectChromosomes(HashSet<Chromosome> population, int generation) {
        Chromosome[] chromosomeArray = population.toArray(new Chromosome[0]);
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

        Chromosome[] winners = winners(randFitness, population.size()).toArray(new Chromosome[0]);
        Chromosome[] loosers = loosers(randFitness, population.size()).toArray(new Chromosome[0]);

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

class ExponentialRankSelection extends Selection {
    @Override
    SelectionResult selectChromosomes(HashSet<Chromosome> population, int generation) {
        Chromosome[] chromosomeArray = population.toArray(new Chromosome[0]);
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

        Chromosome[] winners = winners(randFitness, population.size()).toArray(new Chromosome[0]);
        Chromosome[] loosers = loosers(randFitness, population.size()).toArray(new Chromosome[0]);

        StatsNode sn = new StatsNode(generation, fitnesses);
        return new SelectionResult(winners, loosers, sn);
    }

    HashSet<Chromosome> winners(HashMap<Chromosome, Integer> randFitness, int n) {
        HashSet<Chromosome> result = new HashSet<>();
        Chromosome[] chromosomes = randFitness.keySet().toArray(new Chromosome[0]);
        while (result.size() <= Config.tournamentSize) {
            int pos = Config.random.nextInt(chromosomes.length);
            float lower = ((float) (n * 2 * (n - 1))) / (6 * (n - 1) + n);
            float p = 1.0f * (float) Math.pow(-1 * (randFitness.get(chromosomes[pos]) / lower), Math.E);
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
            float lower = ((float) (n * 2 * (n - 1))) / (6 * (n - 1) + n);
            float p = 1.0f * (float) Math.pow(-1 * (randFitness.get(chromosomes[pos]) / lower), Math.E);
            if (1 - p < Config.random.nextFloat()) {
                result.add(chromosomes[pos]);
            }
        }
        return result;
    }
}

class TournamentSelection extends Selection {
    @Override
    SelectionResult selectChromosomes(HashSet<Chromosome> population, int generation) {
        HashSet<Chromosome> winners = new HashSet<>();
        HashSet<Chromosome> looosers = new HashSet<>();
        HashMap<Chromosome, Float> fitnesses = new HashMap<>();

        Chromosome[] popArr = population.toArray(new Chromosome[0]);
        while (winners.size() <= Config.tournamentSize) {
            Chromosome[] candidates = new Chromosome[Config.numContestants];
            for (int i = 0; i < candidates.length; i++) {
                candidates[i] = popArr[Config.random.nextInt(popArr.length)];
            }
            HashMap<Chromosome, InterpretorResults[]> results = Config.interpretor.run(candidates);
            HashMap<Chromosome, Float> localFitness = new HashMap<>();
            for (Chromosome chromosome : results.keySet()) {
                localFitness.put(chromosome, Fitness.determineFitness(results.get(chromosome)));
            }
            winners.add(winners(localFitness));
            fitnesses.putAll(localFitness);
        }

        while (looosers.size() <= Config.tournamentSize) {
            Chromosome[] candidates = new Chromosome[Config.numContestants];
            for (int i = 0; i < candidates.length; i++) {
                candidates[i] = popArr[Config.random.nextInt(popArr.length)];
            }
            HashMap<Chromosome, InterpretorResults[]> results = Config.interpretor.run(candidates);
            HashMap<Chromosome, Float> localFitness = new HashMap<>();
            for (Chromosome chromosome : results.keySet()) {
                localFitness.put(chromosome, Fitness.determineFitness(results.get(chromosome)));
            }
            winners.add(loosers(localFitness));
            fitnesses.putAll(localFitness);
        }

        StatsNode sn = new StatsNode(generation, fitnesses);
        SelectionResult result = new SelectionResult(winners.toArray(new Chromosome[0]), looosers.toArray(new Chromosome[0]), sn);
        return result;
    }

    Chromosome winners(HashMap<Chromosome, Float> candidates) {
        float bestFitnnes = Float.POSITIVE_INFINITY;
        Chromosome bestChrom = null;
        for (Chromosome chromosome : candidates.keySet()) {
            if (candidates.get(chromosome) < bestFitnnes) {
                bestFitnnes = candidates.get(chromosome);
                bestChrom = chromosome;
            }
        }
        return bestChrom;
    }

    Chromosome loosers(HashMap<Chromosome, Float> candidates) {
        float worstFitness = Float.NEGATIVE_INFINITY;
        Chromosome worstChrom = null;
        for (Chromosome chromosome : candidates.keySet()) {
            if (candidates.get(chromosome) > worstFitness) {
                worstFitness = candidates.get(chromosome);
                worstChrom = chromosome;
            }
        }
        return worstChrom;
    }
}

class TruncationSelection extends Selection{
    @Override
    SelectionResult selectChromosomes(HashSet<Chromosome> population, int generation) {
        Chromosome[] chromosomeArray = population.toArray(new Chromosome[0]);
        HashMap<Chromosome, InterpretorResults[]> results = Config.interpretor.run(chromosomeArray);
        HashMap<Chromosome, Float> fitnesses = new HashMap<>();
        float bestFitness = Float.POSITIVE_INFINITY;
        for (Chromosome chromosome : chromosomeArray) {
            fitnesses.put(chromosome, Fitness.determineFitness(results.get(chromosome)));
            if(bestFitness > fitnesses.get(chromosome)){
                bestFitness = fitnesses.get(chromosome);
            }
        }

        HashSet<Chromosome> possibleWinners = new HashSet<>();
        HashSet<Chromosome> possibleLoosers = new HashSet<>();
        for (Chromosome chromosome : fitnesses.keySet()) {
            if(fitnesses.get(chromosome) < Config.truncationSelectionPer * bestFitness){
                possibleWinners.add(chromosome);
            } else {
                possibleLoosers.add(chromosome);
            }
        }
        StatsNode sn = new StatsNode(generation, fitnesses);
        return new SelectionResult(possibleWinners.toArray(new Chromosome[0]), possibleLoosers.toArray(new Chromosome[0]), sn);
    }
}
