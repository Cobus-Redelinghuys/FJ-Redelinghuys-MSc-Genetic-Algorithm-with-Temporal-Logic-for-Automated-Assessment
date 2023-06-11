import java.util.HashMap;
import java.util.HashSet;

abstract public class Selection {
    abstract SelectionResult selectChromosomes(HashSet<Chromosome> testSet, int generation);
}

class SelectionResult{
    final Chromosome[] winners;
    final Chromosome[] loosers;

    SelectionResult(Chromosome[] w, Chromosome[] l, StatsNode sn){
        winners = w;
        loosers = l;
        statsNode = sn;
    }

    final StatsNode statsNode;
}

class RouletteSelection extends Selection{
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
            sum+=fal;
        }
        HashMap<Chromosome, Float> probabilities = new HashMap<>();
        for (Chromosome chromosome : chromosomeArray) {
            probabilities.put(chromosome, fitnesses.get(chromosome) / sum);
        }

        HashSet<Chromosome> winnersSet = new HashSet<>();
        HashSet<Chromosome> loosersSet = new HashSet<>();
        while(winnersSet.size() <= Config.tournamentSize){
            int pos = Config.random.nextInt(probabilities.size());
            Chromosome candidate = probabilities.keySet().toArray(new Chromosome[0])[pos];
            if(Config.random.nextFloat() <= 1-probabilities.get(candidate)){
                winnersSet.add(candidate);
            }
        }

        while(winnersSet.size() <= Config.tournamentSize){
            int pos = Config.random.nextInt(probabilities.size());
            Chromosome candidate = probabilities.keySet().toArray(new Chromosome[0])[pos];
            if(Config.random.nextFloat() <= probabilities.get(candidate)){
                loosersSet.add(candidate);
            }
        }

        StatsNode sn = new StatsNode(generation, fitnesses);
        return new SelectionResult(winnersSet.toArray(new Chromosome[0]), loosersSet.toArray(new Chromosome[0]), sn);

    }
}