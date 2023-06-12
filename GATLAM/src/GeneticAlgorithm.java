import java.util.ArrayList;
import java.util.HashSet;

public class GeneticAlgorithm {
    HashSet<Chromosome> population = new HashSet<>();
    Stats stats = new Stats();

    public void run() {
        for (int i = 0; i < Config.numGenerations; i++) {
            SelectionResult selectionResult = Config.selectionMethod.selectChromosomes(population, i);
            ArrayList<Chromosome> offspring = new ArrayList<>();
            for (int j = 1; j < selectionResult.winners.length; j++) {
                float prob = Config.random.nextFloat();
                if (prob < Config.crossoverProp) {
                    Chromosome[] crossedOver = Config.crossOver.crossOver(selectionResult.winners[i - 1],
                            selectionResult.winners[i]);
                    prob = Config.random.nextFloat();
                    if (prob < Config.mutationProp) {
                        crossedOver[0] = Config.mutation.mutate(crossedOver[0]);
                    }
                    prob = Config.random.nextFloat();
                    if (prob < Config.mutationProp) {
                        crossedOver[1] = Config.mutation.mutate(crossedOver[1]);
                    }

                    for (Chromosome chromosome : crossedOver) {
                        offspring.add(chromosome);
                    }

                } else {
                    prob = Config.random.nextFloat();
                    if (prob < Config.mutationProp) {
                        offspring.add(Config.mutation.mutate(selectionResult.winners[i]));
                    }
                }
            }

            for (Chromosome chromosome : selectionResult.loosers) {
                if (offspring.size() > 0) {
                    population.remove(chromosome);
                    int replacement = Config.random.nextInt(offspring.size());
                    population.add(offspring.get(replacement));
                }
            }
            stats.addStats(selectionResult.statsNode, population);
        }
    }
}
