import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class GeneticAlgorithm {
    HashSet<Chromosome> population = new HashSet<>();
    Stats stats = new Stats();

    GeneticAlgorithm() {
        while (population.size() < Config.populationSize) {
            population.add(new Chromosome());
        }
    }

    public void run() {
        LocalDateTime start = LocalDateTime.now();
        for (int i = 0; i < Config.numGenerations; i++) {
            LocalDateTime generationStartTime = LocalDateTime.now();
            SelectionResult selectionResult = Config.selectionMethod.selectChromosomes(population, i);
            for(Chromosome chromosome : selectionResult.winners){
                population.remove(chromosome);
                population.add(new Chromosome());
            }
            ChromosomeDatabase.addGenerationInfo();
            LocalDateTime generationEndTime = LocalDateTime.now();
            stats.addStats(selectionResult.statsNode, population,
                    ChronoUnit.MILLIS.between(generationStartTime, generationEndTime));
            System.out.println(stats.getStatsForGeneration(i));
        }
        LocalDateTime endTime = LocalDateTime.now();
        stats.algorithmDuration = ChronoUnit.MILLIS.between(start, endTime);
    }

    void printSummaryToFile(String file) {
        stats.toJSONFile(file);
    }
}
