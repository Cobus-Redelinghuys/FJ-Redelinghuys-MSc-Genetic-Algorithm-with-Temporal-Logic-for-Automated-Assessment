import java.util.HashMap;
import java.util.HashSet;

public class Stats {
    final HashMap<Integer, StatsNode> fitnessSets = new HashMap<>();
    final HashMap<Integer, HashSet<Chromosome>> populationPerGeneration = new HashMap<>();
    public void addStats(StatsNode statsNode, HashSet<Chromosome> population){
        fitnessSets.put(statsNode.generation, statsNode);
    }
}

class StatsNode{
    final int generation;
    final HashMap<Chromosome, Float> fitnessSet;

    StatsNode(int generation, HashMap<Chromosome, Float> fitnessSet){
        this.generation = generation;
        this.fitnessSet = fitnessSet;
    }
}
