import java.util.HashSet;

abstract public class Selection {
    abstract SelectionResult selectChromosomes(HashSet<Chromosome> population, int generation);
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
