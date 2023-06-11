import java.util.HashMap;

public class Stats {
    final HashMap<Integer, StatsNode> fitnessSets = new HashMap<>();
    public void addStats(StatsNode statsNode){
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
